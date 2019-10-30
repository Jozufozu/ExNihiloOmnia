package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloTileEntities
import com.jozufozu.exnihiloomnia.common.network.CCruciblePacket
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import kotlin.math.floor
import kotlin.math.min

class CrucibleTileEntity : TileEntity(ExNihiloTileEntities.CRUCIBLE), ITickableTileEntity {
    private val itemHandler = CrucibleItemHandler()
    val fluidHandler = FluidTank(fluidCapacity)

    var solid: ItemStack = ItemStack.EMPTY
    /** How many mB of solid this crucible has  */
    var solidAmount: Int
        get() = solid.count
        set(value) {
            if (solid.count != value) packet?.solidAmount = value
            solid.count = value
        }

    var solidAmountLastTick: Int = 0

    /** How many mB of fluid this crucible has  */
    val fluidAmount: Int get() = fluidHandler.fluidAmount
    var fluidAmountLastTick: Int = 0

    var partialFluid: Float = 0f
    var partialFluidLastTick: Float = 0f

    /** How hot this crucible currently is  */
    var currentHeatLevel: Int = 0
        set(value) {
            if (value != field) packet?.heat = currentHeatLevel
            field = value
        }

    /** How hot this crucible has to be for its contents to melt  */
    var requiredHeatLevel: Int = 0
    var meltingRatio: Float = 0f

    val fluidContents: FluidStack? get() = this.fluidHandler.fluid.copy()

    val fluidBB get() = AxisAlignedBB(1.0 / 16.0, 3.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, (3.0 / 16.0) + (fluidAmount.toDouble() / fluidCapacity) * (12.0 / 16.0), 15.0 / 16.0)
    val solidBB get() = AxisAlignedBB(1.0 / 16.0, 3.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, (3.0 / 16.0) + (solidAmount.toDouble() / solidCapacity) * (12.0 / 16.0), 15.0 / 16.0)

    /**
     * Accessing packet on the server will populate the backing field if it is null,
     * letting the update loop know for sure that a packet has to be sent.
     * If packet is accessed on the client it will always return null,
     * this makes the side checking by the user unnecessary and can be simplified to just be the ? operator.
     */
    private val packet: CCruciblePacket?
        get() {
            if (world == null || world!!.isRemote) return null
            if (_packet == null) _packet = CCruciblePacket(pos)
            return _packet
        }
    // Separate backing field so the update loop can know whether or not to send a packet
    private var _packet: CCruciblePacket? = null

    override fun tick() {
        solidAmountLastTick = solidAmount
        fluidAmountLastTick = fluidAmount
        partialFluidLastTick = partialFluid

        if (!world!!.isRemote && world!!.gameTime % 100 == 0L) {
            val sourceHeatLevel = RegistryManager.getHeat(world!!.getBlockState(pos.down()))
            //Every 5 seconds, increase the heat by one if the heat source is hotter, decrease it by 1 if it is cooler, or leave it alone
            currentHeatLevel += Integer.signum(sourceHeatLevel - currentHeatLevel)
        }

        if (solidAmount > 0) {
            if (currentHeatLevel >= requiredHeatLevel) {
                val meltingSpeed = currentHeatLevel - requiredHeatLevel

                // We can't melt more than we have or more than we have space for
                val melted = solidAmount
                        .coerceAtMost(meltingSpeed)
                        .coerceAtMost(floor((fluidHandler.capacity - fluidHandler.fluidAmount).toDouble() / meltingRatio.toDouble()).toInt())

                solidAmount -= melted

                var fluid = fluidHandler.fluid

                if (fluid == null) {
                    val meltingRecipe = RegistryManager.getMelting(solid)

                    // Something is pretty wrong
                    if (meltingRecipe == null) {
                        solid = ItemStack.EMPTY
                        meltingRatio = 0f
                        requiredHeatLevel = 0

                        packet?.sendItem()

                        return
                    }

                    fluid = meltingRecipe.output
                    packet?.fluid = fluid
                }

                if (melted > 0) {
                    val actual = melted * meltingRatio + partialFluid
                    val toFill = floor(actual.toDouble()).toInt()
                    partialFluid = actual - toFill

                    fluidHandler.fillInternal(FluidStack(fluid, toFill), true)
                    packet?.sendFluidAmount(fluidAmount, partialFluid)
                }
            }
        } else if (!solid.isEmpty) {
            solid = ItemStack.EMPTY
            meltingRatio = 0f
            requiredHeatLevel = 0

            packet?.sendItem()
        }

        if (!world!!.isRemote) {
            _packet?.let {
                markDirty()
                ExNihiloNetwork.channel.sendToAllAround(it, NetworkRegistry.TargetPoint(world.provider.dimension, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 64.0))
            }
            _packet = null
        }
    }

    override fun getUpdateTag(): CompoundNBT {
        return this.writeToNBT(CompoundNBT())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(getPos(), 1, this.updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        this.readFromNBT(packet.nbtCompound)
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        if (!solid.isEmpty) {
            val solidContents = solid.writeToNBT(CompoundNBT())
            solidContents.removeTag("Count")
            compound.setTag("solidContents", solidContents)
        }

        if (solidAmount != 0) compound.setInteger("solidAmount", solidAmount)

        if (fluidContents != null) compound.setTag("fluidContents", fluidHandler.writeToNBT(CompoundNBT()))

        if (currentHeatLevel != 0) compound.setInteger("currentHeat", currentHeatLevel)
        if (requiredHeatLevel != 0) compound.setInteger("requiredHeat", requiredHeatLevel)
        compound.setFloat("meltingRatio", meltingRatio)
        compound.setFloat("partial", partialFluid)

        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        super.read(compound)

        if (compound.hasKey("solidContents", Constants.NBT.TAG_COMPOUND)) solid = ItemStack(compound.getCompoundTag("solidContents"))
        solidAmount = compound.getInteger("Amount")

        if (compound.hasKey("fluidContents", Constants.NBT.TAG_COMPOUND)) fluidHandler.readFromNBT(compound.getCompoundTag("fluidContents"))

        fluidAmountLastTick = fluidAmount

        currentHeatLevel = compound.getInteger("currentHeat")
        requiredHeatLevel = compound.getInteger("requiredHeat")
        meltingRatio = compound.getFloat("meltingRatio")
        partialFluid = compound.getFloat("partial")
    }

    override fun <T : Any?> getCapability(cap: Capability<T>): LazyOptional<T> {
        return when {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY === cap -> LazyOptional.of { itemHandler }.cast()
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY === cap -> LazyOptional.of { fluidHandler }.cast()
            else -> super.getCapability(cap)
        }
    }

    inner class CrucibleItemHandler : IItemHandler {
        override fun getStackInSlot(slot: Int): ItemStack = solid.copy().also { it.count = 1 }

        override fun getSlotLimit(slot: Int) = solidCapacity

        override fun getSlots() = 1

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty)
                return ItemStack.EMPTY

            val meltingRecipe = RegistryManager.getMelting(stack) ?: return stack

            if (fluidAmount != 0 && !meltingRecipe.output.isFluidEqual(fluidHandler.fluid))
                return stack

            val inputVolume = meltingRecipe.inputVolume

            val allowedIn = min(Math.floorDiv(solidCapacity - solidAmount, inputVolume), stack.count)

            val copy = stack.copy()
            copy.shrink(allowedIn)

            if (!simulate) {
                if (fluidHandler.fluid == null) {
                    fluidHandler.fluid = FluidStack(meltingRecipe.output, 0)
                    packet?.fluid = fluidHandler.fluid
                }

                if (solid.isEmpty) {
                    solid = ItemHandlerHelper.copyStackWithSize(stack, allowedIn * inputVolume)
                    requiredHeatLevel = meltingRecipe.requiredHeat
                    meltingRatio = meltingRecipe.output.amount.toFloat() / inputVolume.toFloat()

                    packet?.sendItem(solid, requiredHeatLevel, meltingRatio)
                } else {
                    solidAmount += allowedIn * inputVolume
                }
            }

            return copy
        }
    }

    companion object {
        val solidCapacity get() = ModConfig.blocks.crucible.solidCapacity
        val fluidCapacity get() = ModConfig.blocks.crucible.fluidCapacity
    }
}
