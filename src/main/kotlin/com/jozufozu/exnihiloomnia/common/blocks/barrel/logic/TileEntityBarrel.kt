package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.FluidEvent
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import kotlin.math.min

class TileEntityBarrel : TileEntity(), ITickable {
    val compostCapacity = ModConfig.blocks.barrel.compostCapacity
    val fluidCapacity = ModConfig.blocks.barrel.fluidCapacity

    var compostAmount: Int = 0
    var compostAmountLastTick: Int = 0

    var color: Color? = null

    var fluidAmount: Int = 0
    var fluidAmountLastTick: Int = 0

    var timer: Int = 0
    var timerLastTick: Int = 0

    var state = BarrelStates.EMPTY
        set(state) {
            val last = this.state
            field = state

            this.state.activate(this, last)
            sync(true)
        }

    private val fluidHandler = BarrelFluidHandler(fluidCapacity)
    private val itemHandler = BarrelItemHandler()

    private var updateTimer: Int = 0
    private var updateNeeded: Boolean = false

    var fluid: FluidStack?
        get() = fluidHandler.fluid
        set(fluid) {
            fluidHandler.fluid = fluid
            sync(true)
        }

    var item: ItemStack
        get() = itemHandler.getStackInSlot(0)
        set(content) {
            itemHandler.setStackInSlot(0, content)
            sync(true)
        }

    private val barrel: TileEntityBarrel
        get() = this

    override fun update() {
        this.timerLastTick = this.timer
        this.fluidAmountLastTick = this.fluidAmount
        this.compostAmountLastTick = this.compostAmount

        this.state.update(this)

        this.fluidAmount = this.fluidHandler.fluidAmount

        if (!world.isRemote) {
            if (updateNeeded && updateTimer == 0) {
                sync()
            }
            updateTimer--
        }
    }

    private fun sync() {
        val pos = getPos()
        val blockState = world.getBlockState(pos)
        world.notifyBlockUpdate(pos, blockState, blockState, 3)

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

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val barrelTag = NBTTagCompound()

        barrelTag.setString("state", this.state.id.toString())

        if (this.color != null) {
            barrelTag.setInteger("color", this.color!!.toInt())
        }

        barrelTag.setTag("itemHandler", itemHandler.serializeNBT())
        barrelTag.setTag("fluidHandler", fluidHandler.writeToNBT(NBTTagCompound()))

        barrelTag.setInteger("compostAmount", this.compostAmount)

        compound.setTag("barrel", barrelTag)

        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        val barrelTag = compound.getCompoundTag("barrel")

        if (barrelTag.hasKey("color")) {
            this.color = Color(barrelTag.getInteger("color"), true)
        }

        this.state = BarrelStates.STATES.getOrDefault(ResourceLocation(barrelTag.getString("state")), BarrelStates.EMPTY)

        this.itemHandler.deserializeNBT(barrelTag.getCompoundTag("itemHandler"))
        this.fluidHandler.readFromNBT(barrelTag.getCompoundTag("fluidHandler"))

        this.compostAmount = barrelTag.getInteger("compostAmount")

        super.readFromNBT(compound)
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

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.state.canInteractWithItems(this) || capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.state.canInteractWithFluids(this)) {
            true
        } else super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.state.canInteractWithItems(this))
            return itemHandler as T

        return if (capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.state.canInteractWithFluids(this)) fluidHandler as T else super.getCapability(capability, facing)

    }

    inner class BarrelItemHandler : ItemStackHandler(1) {

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty)
                return ItemStack.EMPTY

            if (!state.canInteractWithItems(barrel) && !state.canUseItem(barrel, null, null, stack)) {
                return stack
            }

            val enumInteractResult = state.onUseItem(barrel, null, null, stack)

            if (enumInteractResult === EnumInteractResult.CONSUME) {
                return ItemHandlerHelper.copyStackWithSize(stack, stack.count - 1)
            } else if (enumInteractResult === EnumInteractResult.PASS) {
                return stack
            }

            validateSlotIndex(slot)

            val existing = this.stacks[slot]

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
                    this.stacks[slot] = if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, limit) else stack
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

    inner class BarrelFluidHandler(capacity: Int) : FluidTank(capacity) {
        init {
            this.setTileEntity(barrel)
        }

        override fun fillInternal(resource: FluidStack?, doFill: Boolean): Int {
            if (resource == null || resource.amount <= 0 || !state.canInteractWithFluids(barrel) || !state.canFillFluid(barrel, resource)) {
                return 0
            }

            if (!doFill || state.onFillFluid(barrel, resource) === EnumInteractResult.CONSUME) {
                if (fluid == null) {
                    return min(capacity, resource.amount)
                }

                return if (!fluid!!.isFluidEqual(resource)) {
                    0
                } else min(capacity - fluid!!.amount, resource.amount)

            }

            if (fluid == null) {
                fluid = FluidStack(resource, min(capacity, resource.amount))

                onContentsChanged()

                if (tile != null) {
                    FluidEvent.fireEvent(FluidEvent.FluidFillingEvent(fluid, tile.world, tile.pos, this, fluid!!.amount))
                }
                return fluid!!.amount
            }

            if (!fluid!!.isFluidEqual(resource)) {
                return 0
            }
            var filled = capacity - fluid!!.amount

            if (resource.amount < filled) {
                fluid!!.amount += resource.amount
                filled = resource.amount
            } else {
                fluid!!.amount = capacity
            }

            onContentsChanged()

            if (tile != null) {
                FluidEvent.fireEvent(FluidEvent.FluidFillingEvent(fluid, tile.world, tile.pos, this, filled))
            }
            return filled
        }

        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
            if (!state.canInteractWithFluids(barrel)) {
                return null
            }

            val out = super.drain(maxDrain, doDrain)

            if (doDrain) {
                state = BarrelStates.EMPTY
            }

            return out
        }

        override fun drain(resource: FluidStack, doDrain: Boolean): FluidStack? {
            if (!state.canExtractFluids(barrel)) {
                return null
            }

            val out = super.drain(resource, doDrain)

            if (doDrain) {
                state = BarrelStates.EMPTY
            }

            return out
        }

        override fun onContentsChanged() {
            sync(true)
        }
    }
}
