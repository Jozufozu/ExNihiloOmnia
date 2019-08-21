package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloTileEntities
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.set
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import kotlin.math.floor
import kotlin.math.min

class CrucibleTileEntity : TileEntity(ExNihiloTileEntities.CRUCIBLE), ITickableTileEntity {
    var itemHandler = CrucibleItemHandler()
    //var fluidHandler: CrucibleFluidTank

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

//    val fluidContents: FluidStack?
//        get() {
//            val fluid = this.fluidHandler.fluid ?: return null
//
//            return fluid.copy()
//        }

    init {
//        fluidHandler = CrucibleFluidTank(fluidCapacity)
//        fluidHandler.setCanFill(false)
//        fluidHandler.setTileEntity(this)
    }

    override fun tick() {
        solidAmountLastTick = solidAmount
        fluidAmountLastTick = fluidAmount
        partialFluidLastTick = partialFluid

        if (!world!!.isRemote && ticksExisted % 100 == 0) {
            val lastSource = sourceHeatLevel
            val lastCurrent = currentHeatLevel
            sourceHeatLevel = RegistryManager.getHeat(world!!.getBlockState(pos.down()))
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
            var melted = min(solid.count, meltingSpeed)

            //We can't melt more than we have space for
            melted = min(melted, floor(((fluidHandler.capacity - fluidHandler.fluidAmount) / meltingRatio).toDouble()).toInt())

            solid.shrink(melted)
            itemHandler.onContentsChanged(0)

//            var fluid = fluidHandler.fluid
//
//            if (fluid == null) {
//                val meltingRecipe = RegistryManager.getMelting(solid)
//
//                if (meltingRecipe == null)
//                //Something's fucked up
//                {
//                    itemHandler.setStackInSlot(0, ItemStack.EMPTY)
//                    meltingRatio = 0f
//                    requiredHeatLevel = 0
//
//                    ticksExisted++
//                    return
//                }
//
//                fluid = meltingRecipe!!.getOutput()
//            }
            markDirty()

            val actual = melted * meltingRatio + partialFluid
            val `in` = floor(actual.toDouble()).toInt()
            partialFluid = actual - `in`

            //fluidHandler.fillInternal(FluidStack(fluid, `in`), true)
        }

        if (!world!!.isRemote && ticksExisted % 10 == 0 && needsUpdate) {
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
        val blockState = world!!.getBlockState(pos)
        world!!.notifyBlockUpdate(pos, blockState, blockState, 3)
    }

    override fun getUpdateTag(): CompoundNBT {
        return this.write(CompoundNBT())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket {
        return SUpdateTileEntityPacket(getPos(), 1, this.updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SUpdateTileEntityPacket) {
        this.read(packet.nbtCompound)
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        val crucibleData = CompoundNBT()

        val stackInSlot = itemHandler.getStackInSlot(0)
        val solidContents = stackInSlot.write(CompoundNBT())
        solidContents["Count"] = 1
        solidContents["Amount"] = stackInSlot.count

        crucibleData["solidContents"] = solidContents
        //crucibleData["fluidContents"] = fluidHandler.writeToNBT(CompoundNBT())

        crucibleData["currentHeat"] = currentHeatLevel
        crucibleData["sourceHeat"] = sourceHeatLevel
        crucibleData["requiredHeat"] = requiredHeatLevel
        crucibleData["meltingRatio"] = meltingRatio
        crucibleData["partial"] = partialFluid

        compound["crucibleData"] = crucibleData
        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        super.read(compound)

        val crucibleData = compound.getCompound("crucibleData")

        val solidContents = crucibleData.getCompound("solidContents")

        val solid = ItemStack.read(solidContents)
        solid.count = solidContents.getInt("Amount")

        itemHandler.setStackInSlot(0, solid)

//        fluidHandler.readFromNBT(crucibleData.getCompound("fluidContents"))
//
//        fluidAmount = fluidHandler.fluidAmount
//        fluidAmountLastTick = fluidAmount

        currentHeatLevel = crucibleData.getInt("currentHeat")
        sourceHeatLevel = crucibleData.getInt("sourceHeat")
        requiredHeatLevel = crucibleData.getInt("requiredHeat")
        meltingRatio = crucibleData.getFloat("meltingRatio")
        partialFluid = crucibleData.getFloat("partial")
    }

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY === capability)
            return itemHandler as T

        //return if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY === capability) fluidHandler as T else super.getCapability(capability, facing)
    }

    inner class CrucibleItemHandler : ItemStackHandler(1) {

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            return ItemStack.EMPTY
        }

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty)
                return ItemStack.EMPTY

            val meltingRecipe = RegistryManager.getMelting(stack) ?: return stack

            if (fluidAmount != 0 && !meltingRecipe!!.getOutput().isFluidEqual(fluidHandler.fluid))
                return stack

            val existing = this.stacks[slot]

            val inputVolume = meltingRecipe!!.getInputVolume()

            var allowedIn = Math.floorDiv(solidCapacity - existing.count, inputVolume)

            allowedIn = Math.min(allowedIn, stack.count)

            val copy = stack.copy()
            copy.shrink(allowedIn)

            if (!simulate) {
                requiredHeatLevel = meltingRecipe!!.getRequiredHeat()
                meltingRatio = meltingRecipe!!.getOutput().amount as Float / inputVolume.toFloat()

                if (fluidHandler.fluid == null)
                    fluidHandler.fluid = FluidStack(meltingRecipe!!.getOutput(), 0)

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

//    inner class CrucibleFluidTank(capacity: Int) : FluidTank(capacity) {
//
//        override fun fill(resource: FluidStack, doFill: Boolean): Int {
//            sync()
//            return super.fill(resource, doFill)
//        }
//
//        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack {
//            sync()
//            return super.drain(maxDrain, doDrain)
//        }
//
//        override fun onContentsChanged() {
//            fluidAmount = fluidAmount
//            markDirty()
//        }
//    }
}
