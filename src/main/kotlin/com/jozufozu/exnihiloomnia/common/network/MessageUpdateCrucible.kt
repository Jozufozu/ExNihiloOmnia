package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessageUpdateCrucible() : IMessage {
    constructor(pos: BlockPos): this() {
        this.pos = pos
    }

    lateinit var pos: BlockPos

    private var item: ItemStack? = null
    private var requiredHeat: Int = 0
    private var meltingRatio: Float = 0f

    var solidAmount: Int? = null

    var fluid: FluidStack? = null

    var fluidAmount: Int? = null
    var partialFluidAmount: Float = 0f

    var heat: Int? = null

    fun sendItem(stack: ItemStack, requiredHeat: Int, meltingRatio: Float) {
        item = stack
        this.requiredHeat = requiredHeat
        this.meltingRatio = meltingRatio
    }

    fun sendItem() {
        item = ItemStack.EMPTY
    }

    fun sendFluidAmount(fluidAmount: Int, partial: Float) {
        this.fluidAmount = fluidAmount
        this.partialFluidAmount = partial
    }

    override fun fromBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)
        pos = buf.readBlockPos()
        val mask = buf.readByte().toInt()

        if ((mask and itemMask) != 0) {
            val stack = buf.readStackWithoutSize()

            if (!stack.isEmpty) {
                requiredHeat = buf.readVarInt()
                meltingRatio = buf.readFloat()
            }

            item = stack
        }

        if ((mask and solidAmountMask) != 0) solidAmount = buf.readInt()

        if ((mask and fluidMask) != 0) {
            val fluidName = buf.readString(64)
            val tag = buf.readCompoundTag()

            fluid = FluidStack(FluidRegistry.getFluid(fluidName), 0, tag)
        }

        if ((mask and fluidAmountMask) != 0) {
            fluidAmount = buf.readInt()
            partialFluidAmount = buf.readFloat()
        }

        if ((mask and heatMask) != 0) heat = buf.readVarInt()
    }

    override fun toBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)

        var mask = 0
        if (item != null) mask = mask or itemMask
        if (solidAmount != null) mask = mask or solidAmountMask

        if (fluid != null) mask = mask or fluidMask
        if (fluidAmount != null) mask = mask or fluidAmountMask

        if (heat != null) mask = mask or heatMask

        buf.writeBlockPos(pos)
        buf.writeByte(mask)

        item?.let {
            buf.writeStackWithoutSize(it)

            if (!it.isEmpty) {
                buf.writeVarInt(requiredHeat)
                buf.writeFloat(meltingRatio)
            }
        }
        solidAmount?.let { buf.writeInt(it) }

        fluid?.let {
            buf.writeString(it.fluid.name)
            buf.writeCompoundTag(it.tag)
        }
        fluidAmount?.let {
            buf.writeInt(it)
            buf.writeFloat(partialFluidAmount)
        }

        heat?.let { buf.writeVarInt(it) }
    }

    private fun PacketBuffer.writeStackWithoutSize(stack: ItemStack) {
        if (stack.isEmpty) {
            this.writeVarInt(-1)
        } else {
            this.writeVarInt(Item.getIdFromItem(stack.item))
            this.writeShort(stack.metadata)
            var nbttagcompound: NBTTagCompound? = null

            if (stack.item.isDamageable || stack.item.shareTag) {
                nbttagcompound = stack.item.getNBTShareTag(stack)
            }

            this.writeCompoundTag(nbttagcompound)
        }
    }

    private fun PacketBuffer.readStackWithoutSize(): ItemStack {
        val i = this.readVarInt()

        return if (i < 0) {
            ItemStack.EMPTY
        } else {
            val k = this.readShort().toInt()
            val itemstack = ItemStack(Item.getItemById(i), 1, k)
            itemstack.item.readNBTShareTag(itemstack, this.readCompoundTag())
            itemstack
        }
    }

    class Handler : IMessageHandler<MessageUpdateCrucible, IMessage> {
        override fun onMessage(msg: MessageUpdateCrucible, ctx: MessageContext): IMessage? {
            val mc = Minecraft.getMinecraft()

            mc.addScheduledTask {
                if (!mc.world.isBlockLoaded(msg.pos)) return@addScheduledTask

                (mc.world.getTileEntity(msg.pos) as? TileEntityCrucible)?.let { crucible ->
                    msg.item?.let {
                        crucible.solid = it
                        crucible.requiredHeatLevel = msg.requiredHeat
                        crucible.meltingRatio = msg.meltingRatio
                    }
                    msg.solidAmount?.let { crucible.solidAmount = it }
                    msg.fluid?.let { crucible.fluidHandler.fluid = it }
                    msg.fluidAmount?.let {
                        crucible.fluidHandler.fluid?.amount = it
                        crucible.partialFluid = msg.partialFluidAmount
                    }
                    msg.heat?.let { crucible.currentHeatLevel = it }
                }
            }

            return null
        }
    }

    companion object {
        const val itemMask: Int = 0b1
        const val solidAmountMask: Int = 0b10
        const val fluidMask: Int = 0b100
        const val fluidAmountMask: Int = 0b1000
        const val heatMask: Int = 0b10000
    }
}