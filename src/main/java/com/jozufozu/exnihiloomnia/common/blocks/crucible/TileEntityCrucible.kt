package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

class TileEntityCrucible : TileEntity(), ITickable {
    var itemHandler = CrucibleItemHandler()
    var fluidHandler: CrucibleFluidTank

    val solidCapacity = ModConfig.blocks.crucible.solidCapacity
    val fluidCapacity = ModConfig.blocks.crucible.fluidCapacity

    /** How many mB of solid this crucible has  */
    var solidAmount: Int = 0
    var solidAmountLastTick: Int = 0

    /** How many mB of fluid this crucible has  */
    var fluidAmount: Int = 0
    var fluidAmountLastTick: Int = 0

    var partialFluid: Float = 0.toFloat()
    var partialFluidLastTick: Float = 0.toFloat()

    /** How hot this crucible currently is  */
    var currentHeatLevel: Int = 0

    /** How hot this crucible's heat source is  */
    var sourceHeatLevel: Int = 0

    /** How hot this crucible has to be for its contents to melt  */
    var requiredHeatLevel: Int = 0

    var meltingRatio: Float = 0.toFloat()

    private var ticksExisted: Int = 0

    private var needsUpdate: Boolean = false

    val solidContents: ItemStack
        get() = this.itemHandler.getStackInSlot(0).copy()

    val fluidContents: FluidStack?
        get() {
            val fluid = this.fluidHandler.fluid ?: return null

            return fluid.copy()
        }

    init {
        fluidHandler = CrucibleFluidTank(fluidCapacity)
        fluidHandler.setCanFill(false)
        fluidHandler.setTileEntity(this)
    }

    override fun update() {
        solidAmountLastTick = solidAmount
        fluidAmountLastTick = fluidAmount
        partialFluidLastTick = partialFluid

        if (!world.isRemote && ticksExisted % 100 == 0) {
            val lastSource = sourceHeatLevel
            val lastCurrent = currentHeatLevel
            sourceHeatLevel = RegistryManager.getHeat(world.getBlockState(pos.down()))
            //Every 5 seconds, increase the heat by one if the heat source is hotter, decrease it by 1 if it is cooler, or leave it alone
            currentHeatLevel += Integer.signum(sourceHeatLevel - currentHeatLevel)

            if (lastCurrent != currentHeatLevel || lastSource != sourceHeatLevel) {
                markDirty()
            }
        }

        if (solidAmount > 0 && currentHeatLevel >= requiredHeatLevel) {
            val meltingSpeed = currentHeatLevel - requiredHeatLevel

            val solid = itemHandler.getStackInSlot(0)

            //We can't melt more than we have
            var melted = Math.min(solid.count, meltingSpeed)

            //We can't melt more than we have space for
            melted = Math.min(melted, Math.floor(((fluidHandler.capacity - fluidHandler.fluidAmount) / meltingRatio).toDouble()).toInt())

            solid.shrink(melted)
            itemHandler.onContentsChanged(0)

            var fluid = fluidHandler.fluid

            if (fluid == null) {
                val meltingRecipe = RegistryManager.getMelting(solid)

                if (meltingRecipe == null)
                //Something's fucked up
                {
                    itemHandler.setStackInSlot(0, ItemStack.EMPTY)
                    meltingRatio = 0f
                    requiredHeatLevel = 0

                    ticksExisted++
                    return
                }

                fluid = meltingRecipe.output
            }
            markDirty()

            val actual = melted * meltingRatio + partialFluid
            val `in` = Math.floor(actual.toDouble()).toInt()
            partialFluid = actual - `in`

            fluidHandler.fillInternal(FluidStack(fluid!!, `in`), true)
        }

        if (!world.isRemote && ticksExisted % 10 == 0 && needsUpdate) {
            sync()
            needsUpdate = false
        }

        ticksExisted++
    }

    override fun markDirty() {
        super.markDirty()
        this.needsUpdate = true
    }

    fun sync() {
        val pos = getPos()
        val blockState = world.getBlockState(pos)
        world.notifyBlockUpdate(pos, blockState, blockState, 3)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return this.writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(getPos(), 1, this.updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        this.readFromNBT(packet.nbtCompound)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val crucibleData = NBTTagCompound()

        val stackInSlot = itemHandler.getStackInSlot(0)
        val solidContents = stackInSlot.writeToNBT(NBTTagCompound())
        solidContents.setByte("Count", 1.toByte())
        solidContents.setInteger("Amount", stackInSlot.count)

        crucibleData.setTag("solidContents", solidContents)
        crucibleData.setTag("fluidContents", fluidHandler.writeToNBT(NBTTagCompound()))

        crucibleData.setInteger("currentHeat", currentHeatLevel)
        crucibleData.setInteger("sourceHeat", sourceHeatLevel)
        crucibleData.setInteger("requiredHeat", requiredHeatLevel)
        crucibleData.setFloat("meltingRatio", meltingRatio)
        crucibleData.setFloat("partial", partialFluid)

        compound.setTag("crucibleData", crucibleData)
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)

        val crucibleData = compound.getCompoundTag("crucibleData")

        val solidContents = crucibleData.getCompoundTag("solidContents")

        val solid = ItemStack(solidContents)
        solid.count = solidContents.getInteger("Amount")

        itemHandler.setStackInSlot(0, solid)

        fluidHandler.readFromNBT(crucibleData.getCompoundTag("fluidContents"))

        fluidAmount = fluidHandler.fluidAmount
        fluidAmountLastTick = fluidAmount

        currentHeatLevel = crucibleData.getInteger("currentHeat")
        sourceHeatLevel = crucibleData.getInteger("sourceHeat")
        requiredHeatLevel = crucibleData.getInteger("requiredHeat")
        meltingRatio = crucibleData.getFloat("meltingRatio")
        partialFluid = crucibleData.getFloat("partial")
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY === capability || CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY === capability || super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY === capability)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler)

        return if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY === capability) CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler) else super.getCapability(capability, facing)

    }

    inner class CrucibleItemHandler : ItemStackHandler(1) {

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            return ItemStack.EMPTY
        }

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty)
                return ItemStack.EMPTY

            val meltingRecipe = RegistryManager.getMelting(stack) ?: return stack

            if (fluidAmount != 0 && !meltingRecipe.output.isFluidEqual(fluidHandler.fluid))
                return stack

            val existing = this.stacks[slot]

            val inputVolume = meltingRecipe.inputVolume

            var allowedIn = Math.floorDiv(solidCapacity - existing.count, inputVolume)

            allowedIn = Math.min(allowedIn, stack.count)

            val copy = stack.copy()
            copy.shrink(allowedIn)

            if (!simulate) {
                requiredHeatLevel = meltingRecipe.requiredHeat
                meltingRatio = meltingRecipe.output.amount.toFloat() / inputVolume.toFloat()

                if (fluidHandler.fluid == null)
                    fluidHandler.fluid = FluidStack(meltingRecipe.output, 0)

                if (existing.isEmpty) {
                    this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(stack, allowedIn * inputVolume)
                } else {
                    existing.grow(allowedIn * inputVolume)
                }
                onContentsChanged(slot)
                sync()
            }

            return copy
        }

        override fun getStackLimit(slot: Int, stack: ItemStack): Int {
            return solidCapacity
        }

        override fun onLoad() {
            solidAmount = getStackInSlot(0).count
            solidAmountLastTick = solidAmount
        }

        public override fun onContentsChanged(slot: Int) {
            solidAmount = getStackInSlot(slot).count
            markDirty()
        }
    }

    inner class CrucibleFluidTank(capacity: Int) : FluidTank(capacity) {

        override fun fill(resource: FluidStack, doFill: Boolean): Int {
            sync()
            return super.fill(resource, doFill)
        }

        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
            sync()
            return super.drain(maxDrain, doDrain)
        }

        override fun onContentsChanged() {
            this@TileEntityCrucible.fluidAmount = fluidAmount
            markDirty()
        }
    }
}
