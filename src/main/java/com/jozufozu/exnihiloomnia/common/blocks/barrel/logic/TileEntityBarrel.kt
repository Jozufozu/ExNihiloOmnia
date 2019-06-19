package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.set
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

class TileEntityBarrel : TileEntity(tileEntityTypeIn), ITickableTileEntity {
    val compostCapacity = ModConfig.blocks.barrel.compostCapacity
    val fluidCapacity = ModConfig.blocks.barrel.fluidCapacity

    var compostAmount: Int = 0
    var compostAmountLastTick: Int = 0

    var color: Color? = null

    var fluidAmount: Int = 0
    var fluidAmountLastTick: Int = 0

    var timer: Int = 0
    var timerLastTick: Int = 0

    var state: BarrelState = BarrelStates.EMPTY
        set(state) {
            val last = state
            field = state

            world?.let { state.activate(this, it, last) }
            sync(true)
        }

//    private val fluidHandler = FluidTankBarrel(fluidCapacity)
//    private val fluidHolder = LazyOptional.of<IFluidHandler> { fluidHandler }
    private val itemHandler = ItemStackHandlerBarrel()
    private val itemHolder = LazyOptional.of<IItemHandler> { itemHandler }

    private var updateTimer: Int = 0
    private var updateNeeded: Boolean = false

//    var fluid: FluidStack?
//        get() = fluidHandler.fluid
//        set(fluid) {
//            fluidHandler.fluid = fluid
//            sync(true)
//        }

    var item: ItemStack
        get() = itemHandler.getStackInSlot(0)
        set(content) {
            itemHandler.setStackInSlot(0, content)
            sync(true)
        }

    private val barrel: TileEntityBarrel
        get() = this

    override fun tick() {
        timerLastTick = timer
        fluidAmountLastTick = fluidAmount
        compostAmountLastTick = compostAmount

        state.update(this, world!!)

        //fluidAmount = fluidHandler.fluidAmount

        if (world?.isRemote == false) {
            if (updateNeeded && updateTimer == 0) {
                sync()
            }
            updateTimer--
        }
    }

    private fun sync() {
        val pos = getPos()
        val blockState = world!!.getBlockState(pos)
        world!!.notifyBlockUpdate(pos, blockState, blockState, 3)

        updateNeeded = false
    }

    fun sync(now: Boolean) {
        if (now) {
            updateNeeded = true
            updateTimer = 0
        } else if (!updateNeeded) {
            updateNeeded = true
            updateTimer = 10
        }
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        val barrelTag = CompoundNBT()

        barrelTag["state"] = state.id.toString()

        if (color != null) {
            barrelTag["color"] = color!!.toInt()
        }

        barrelTag["itemHandler"] = itemHandler.serializeNBT()
        //barrelTag["fluidHandler"] = fluidHandler.writeToNBT(CompoundNBT())

        barrelTag["compostAmount"] = compostAmount

        compound["barrel"] = barrelTag

        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        val barrelTag = compound.getCompound("barrel")

        if ("color" in barrelTag) {
            color = Color(barrelTag.getInt("color"), true)
        }

        state = BarrelStates[ResourceLocation(barrelTag.getString("state"))]

        itemHandler.deserializeNBT(barrelTag.getCompound("itemHandler"))
        //fluidHandler.readFromNBT(barrelTag.getCompound("fluidHandler"))

        compostAmount = barrelTag.getInt("compostAmount")

        super.read(compound)
    }

    override fun getUpdateTag() = write(CompoundNBT())

    override fun getUpdatePacket() = SUpdateTileEntityPacket(getPos(), 1, updateTag)

    override fun onDataPacket(net: NetworkManager, packet: SUpdateTileEntityPacket) {
        read(packet.nbtCompound)
    }

    override fun <T> getCapability(capability: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && state.canInteractWithItems(this)) itemHolder.cast() else super.getCapability(capability, side)

        //return if (capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canInteractWithFluids(this)) fluidHolder.cast() else super.getCapability(capability, side)
    }

    inner class ItemStackHandlerBarrel : ItemStackHandler(1) {

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty)
                return ItemStack.EMPTY

            if (!state.canInteractWithItems(barrel) && !state.canUseItem(barrel, world!!, stack, null, null)) {
                return stack
            }

            val enumInteractResult = state.onUseItem(barrel, world!!, stack, null, null)

            if (enumInteractResult == InteractResult.CONSUME) {
                return ItemHandlerHelper.copyStackWithSize(stack, stack.count - 1)
            } else if (enumInteractResult == InteractResult.PASS) {
                return stack
            }

            validateSlotIndex(slot)

            val existing = stacks[slot]

            var limit = getStackLimit(slot, stack)

            if (!existing.isEmpty) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack

                limit -= existing.count
            }

            if (limit <= 0)
                return stack

            val reachedLimit = stack.count > limit

            if (!simulate) {
                if (existing.isEmpty) {
                    stacks[slot] = if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, limit) else stack
                } else {
                    existing.grow(if (reachedLimit) limit else stack.count)
                }
                onContentsChanged(slot)
            }

            return if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, stack.count - limit) else ItemStack.EMPTY
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            if (!state.canExtractItems(barrel)) {
                return ItemStack.EMPTY
            }

            val out = super.extractItem(slot, amount, simulate)

            if (!simulate) {
                state = BarrelStates.EMPTY
            }

            return out
        }

        override fun onContentsChanged(slot: Int) {
            sync(true)
        }
    }

//    inner class FluidTankBarrel(capacity: Int) : FluidTank(capacity) {
//        init {
//            setTileEntity(barrel)
//        }
//
//        override fun fillInternal(resource: FluidStack, doFill: Boolean): Int {
//            if (resource.amount <= 0 || !state.canInteractWithFluids(barrel) || !state.canFillFluid(barrel, world!!, resource)) {
//                return 0
//            }
//
//            if (!doFill || state.onFillFluid(barrel, world!!, resource) == InteractResult.CONSUME) {
//                if (fluid == null) {
//                    return Math.min(capacity, resource.amount)
//                }
//
//                return if (!fluid!!.isFluidEqual(resource)) {
//                    0
//                } else Math.min(capacity - fluid!!.amount, resource.amount)
//
//            }
//
//            if (fluid == null) {
//                fluid = FluidStack(resource, Math.min(capacity, resource.amount))
//
//                onContentsChanged()
//
//                if (tile != null) {
//                    FluidEvent.fireEvent(FluidEvent.FluidFillingEvent(fluid, tile.world, tile.pos, this, fluid!!.amount))
//                }
//                return fluid!!.amount
//            }
//
//            if (!fluid!!.isFluidEqual(resource)) {
//                return 0
//            }
//            var filled = capacity - fluid!!.amount
//
//            if (resource.amount < filled) {
//                fluid!!.amount += resource.amount
//                filled = resource.amount
//            } else {
//                fluid!!.amount = capacity
//            }
//
//            onContentsChanged()
//
//            if (tile != null) {
//                FluidEvent.fireEvent(FluidEvent.FluidFillingEvent(fluid, tile.world, tile.pos, this, filled))
//            }
//            return filled
//        }
//
//        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
//            if (!state.canInteractWithFluids(barrel)) {
//                return null
//            }
//
//            val out = super.drain(maxDrain, doDrain)
//
//            if (doDrain) {
//                state = BarrelStates.EMPTY
//            }
//
//            return out
//        }
//
//        override fun drain(resource: FluidStack, doDrain: Boolean): FluidStack? {
//            if (!state.canExtractFluids(barrel)) {
//                return null
//            }
//
//            val out = super.drain(resource, doDrain)
//
//            if (doDrain) {
//                state = BarrelStates.EMPTY
//            }
//
//            return out
//        }
//
//        override fun onContentsChanged() {
//            sync(true)
//        }
//    }
}
