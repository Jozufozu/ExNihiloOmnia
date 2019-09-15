package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.network.MessageUpdateBarrel
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.material.Material
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
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import java.util.*
import kotlin.math.min

class TileEntityBarrel : TileEntity(), ITickable {
    /**
     * When set, marks the compost level to be sent to the client
     */
    var compostAmount: Int = 0
        set(value) {
            field = value
            packet?.compostLevel = Optional.of(value)
        }
    var compostAmountLastTick: Int = 0

    /**
     * When set, marks the color to be sent to the client
     */
    var color: Color = Color.WHITE
        set(value) {
            field = value
            packet?.color = Optional.of(value.toInt())
        }

    var fluidAmount: Int = 0
    var fluidAmountLastTick: Int = 0

    var timer: Int = 0
    var timerLastTick: Int = 0

    var burnTimer: Int = 0

    /**
     * Barrels are state machines and this field represents the logical state the barrel is in.
     *
     * When set, marks the state to be sent to the client
     */
    var state: BarrelState = BarrelStates.EMPTY
        set(state) {
            field = state
            state.activate(this)

            packet?.barrelState = Optional.of(state.id.toString())
        }

    /**
     * Accessing packet on the server will populate the backing field if it is null,
     * letting the update loop know for sure that a packet has to be sent.
     * If packet is accessed on the client it will always return null,
     * this makes the side checking by the user unnecessary and can be simplified to just be the ? operator.
     */
    private val packet: MessageUpdateBarrel?
        get() {
            if (world == null || world.isRemote) return null
            if (_packet == null) _packet = MessageUpdateBarrel(pos)
            return _packet
        }
    // Separate backing field so the update loop can know whether or not to send a packet
    private var _packet: MessageUpdateBarrel? = null

    /**
     * When set, marks the fluid to be sent to the client
     */
    var fluid: FluidStack?
        get() = fluidHandler.fluid
        set(value) {
            fluidHandler.fluid = value
        }

    /**
     * When set, marks the item to be sent to the client
     */
    var item: ItemStack
        get() = itemHandler.getStackInSlot(0)
        set(value) {
            itemHandler.setStackInSlot(0, value)
            packet?.item = Optional.of(value)
        }

    val material: Material get() = world.getBlockState(pos).material

    private val fluidHandler = BarrelFluidHandler(fluidCapacity)
    private val itemHandler = BarrelItemHandler()
    private val barrel: TileEntityBarrel
        get() = this

    override fun update() {
        this.timerLastTick = this.timer
        this.fluidAmountLastTick = this.fluidAmount
        this.compostAmountLastTick = this.compostAmount

        this.state.update(this)

        this.fluidAmount = this.fluidHandler.fluidAmount

        if (!world.isRemote) {
            _packet?.let {
                markDirty()
                ExNihiloNetwork.channel.sendToAllAround(it, NetworkRegistry.TargetPoint(world.provider.dimension, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 64.0))
            }
            _packet = null
        }
    }

    fun resetBurnTimer() {
        burnTimer = 0
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val barrelTag = NBTTagCompound()

        barrelTag.setString("state", this.state.id.toString())
        barrelTag.setInteger("color", this.color.toInt())

        item.takeIf { !it.isEmpty }?.let { barrelTag.setTag("item", it.writeToNBT(NBTTagCompound())) }
        fluid?.let { barrelTag.setTag("fluid", it.writeToNBT(NBTTagCompound())) }
        if (compostAmount != 0) barrelTag.setInteger("compostAmount", this.compostAmount)
        if (burnTimer != 0) barrelTag.setInteger("burnTimer", this.burnTimer)

        compound.setTag("barrel", barrelTag)

        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        val barrelTag = compound.getCompoundTag("barrel")

        color = Color(barrelTag.getInteger("color"), true)

        state = BarrelStates.STATES.getOrDefault(ResourceLocation(barrelTag.getString("state")), BarrelStates.EMPTY)

        item = if (barrelTag.hasKey("item")) ItemStack(barrelTag.getCompoundTag("item")) else ItemStack.EMPTY
        fluid = if (barrelTag.hasKey("fluid")) FluidStack.loadFluidStackFromNBT(barrelTag.getCompoundTag("fluid")) else null

        compostAmount = barrelTag.getInteger("compostAmount")
        burnTimer = barrelTag.getInteger("burnTimer")

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
        return when {
            capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.state.canInteractWithItems(this) -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler)
            capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.state.canInteractWithFluids(this) -> CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler)
            else -> super.getCapability(capability, facing)
        }
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
            packet?.item = Optional.of(getStackInSlot(0))
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

        override fun setFluid(fluid: FluidStack?) {
            super.setFluid(fluid)
            onContentsChanged()
        }

        override fun onContentsChanged() {
            packet?.updateFluid = true
            packet?.fluid = fluid
        }
    }

    companion object {
        val compostCapacity get() = ModConfig.blocks.barrel.compostCapacity
        val fluidCapacity get() = ModConfig.blocks.barrel.fluidCapacity
        val burnTemperature get() = ModConfig.blocks.barrel.burnTemperature
        val burnTime get() = ModConfig.blocks.barrel.burnTime
    }
}
