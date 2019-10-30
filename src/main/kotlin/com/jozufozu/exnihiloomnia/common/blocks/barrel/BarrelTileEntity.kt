package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloTileEntities
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.EnumInteractResult
import com.jozufozu.exnihiloomnia.common.network.CBarrelPacket
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

class BarrelTileEntity : TileEntity(ExNihiloTileEntities.BARREL), ITickableTileEntity {
    /**
     * When set, marks the compost level to be sent to the client
     */
    var compostAmount: Int = 0
        set(value) {
            field = value
            packet?.compostLevel = value
        }
    var compostAmountLastTick: Int = 0

    /**
     * When set, marks the color to be sent to the client
     */
    var color: Color = Color.WHITE
        set(value) {
            field = value
            packet?.color = value.toInt()
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

            packet?.barrelState = state.id.toString()
        }

    /**
     * Accessing packet on the server will populate the backing field if it is null,
     * letting the update loop know for sure that a packet has to be sent.
     * If packet is accessed on the client it will always return null,
     * this makes the side checking by the user unnecessary and can be simplified to just be the ? operator.
     */
    private val packet: CBarrelPacket?
        get() {
            if (world == null || world!!.isRemote) return null
            if (_packet == null) _packet = CBarrelPacket(pos)
            return _packet
        }
    // Separate backing field so the update loop can know whether or not to send a packet
    private var _packet: CBarrelPacket? = null

    /**
     * When set, marks the fluid to be sent to the client
     */
    var fluid: FluidStack
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
            packet?.item = value
        }

    val material: Material get() = blockState.material
    val fluidBB get() = AxisAlignedBB(2.0 / 16.0, 1.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, (1.0 / 16.0) + (fluid.amount.toDouble() / fluidCapacity) * (14.0 / 16.0), 14.0 / 16.0)
    val compostBB get() = AxisAlignedBB(2.0 / 16.0, 1.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, (1.0 / 16.0) + (barrel.compostAmount.toDouble() / compostCapacity) * (14.0 / 16.0), 14.0 / 16.0)

    private val fluidHandler = BarrelFluidHandler(fluidCapacity)
    private val itemHandler = BarrelItemHandler()
    private val barrel: BarrelTileEntity get() = this

    override fun tick() {
        this.timerLastTick = this.timer
        this.fluidAmountLastTick = this.fluidAmount
        this.compostAmountLastTick = this.compostAmount

        this.state.update(this)

        this.fluidAmount = this.fluidHandler.fluidAmount

        if (!world!!.isRemote) {
            _packet?.let {
                markDirty()
                ExNihiloNetwork.channel.send(PacketDistributor.TRACKING_CHUNK.with { world!!.getChunkAt(pos) }, it)
            }
            _packet = null
        }
    }

    fun resetBurnTimer() {
        burnTimer = 0
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        val barrelTag = CompoundNBT()

        barrelTag.putString("state", state.id.toString())
        barrelTag.putInt("color", color.toInt())

        item.takeIf { !it.isEmpty }?.let { barrelTag.put("item", it.write(CompoundNBT())) }
        fluid.takeIf { !it.isEmpty }?.let { barrelTag.put("fluid", it.writeToNBT(CompoundNBT())) }
        if (compostAmount != 0) barrelTag.putInt("compostAmount", compostAmount)
        if (burnTimer != 0) barrelTag.putInt("burnTimer", burnTimer)

        compound.put("barrel", barrelTag)

        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        val barrelTag = compound.getCompound("barrel")

        color = Color(barrelTag.getInt("color"), true)

        state = BarrelStates.STATES.getOrDefault(ResourceLocation(barrelTag.getString("state")), BarrelStates.EMPTY)

        item = if ("item" in barrelTag) ItemStack.read(barrelTag.getCompound("item")) else ItemStack.EMPTY
        fluid = if ("fluid" in barrelTag) FluidStack.loadFluidStackFromNBT(barrelTag.getCompound("fluid")) else FluidStack.EMPTY

        compostAmount = barrelTag.getInt("compostAmount")
        burnTimer = barrelTag.getInt("burnTimer")

        super.read(compound)
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

    override fun <T : Any?> getCapability(cap: Capability<T>): LazyOptional<T> {
        return when {
            cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && state.canInteractWithItems(this) -> LazyOptional.of { itemHandler }.cast()
            cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canInteractWithFluids(this) -> LazyOptional.of { fluidHandler }.cast()
            else -> super.getCapability(cap)
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
            packet?.item = getStackInSlot(0)
        }
    }

    inner class BarrelFluidHandler(capacity: Int) : FluidTank(capacity) {
        init {
            setValidator {
                !state.canInteractWithFluids(barrel) || !state.canFillFluid(barrel, it)
            }
        }

        override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
            if (!state.canInteractWithFluids(barrel) || !state.canExtractFluids(barrel)) {
                return FluidStack.EMPTY
            }

            val out = super.drain(maxDrain, action)

            if (action.execute()) {
                state = BarrelStates.EMPTY
            }

            return out
        }

        override fun setFluid(fluid: FluidStack) {
            super.setFluid(fluid)
            onContentsChanged()
        }

        override fun onContentsChanged() {
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
