package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.util.Color
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

class MessageUpdateBarrel() : IMessage {
    constructor(pos: BlockPos): this() {
        this.pos = pos
    }

    lateinit var pos: BlockPos
    var barrelState: Optional<String> = Optional.empty()
    var item: Optional<ItemStack> = Optional.empty()
    var updateFluid: Boolean = false
    var fluid: FluidStack? = null
    var compostLevel: Optional<Int> = Optional.empty()
    var color: Optional<Int> = Optional.empty()

    override fun fromBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)
        pos = buf.readBlockPos()
        val mask = buf.readByte()

        if ((mask and barrelStateMask) != 0.toByte()) barrelState = Optional.of(buf.readString(64))
        if ((mask and itemMask) != 0.toByte()) item = Optional.of(buf.readItemStack())
        if ((mask and fluidMask) != 0.toByte()) {
            updateFluid = true
            if (buf.readBoolean()) {
                val fluidName = buf.readString(64)
                val amount = buf.readVarInt()
                val tag = buf.readCompoundTag()

                fluid = FluidStack(FluidRegistry.getFluid(fluidName), amount, tag)
            }
        }
        if ((mask and compostLevelMask) != 0.toByte()) compostLevel = Optional.of(buf.readVarInt())
        if ((mask and colorMask) != 0.toByte()) color = Optional.of(buf.readInt())
    }

    override fun toBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)

        var mask: Byte = 0
        barrelState.ifPresent { mask = mask or barrelStateMask }
        item.ifPresent { mask = mask or itemMask }
        if (updateFluid) { mask = mask or fluidMask }
        compostLevel.ifPresent { mask = mask or compostLevelMask }
        color.ifPresent { mask = mask or colorMask }

        buf.writeBlockPos(pos)
        buf.writeByte(mask.toInt())

        barrelState.ifPresent { buf.writeString(it) }
        item.ifPresent { buf.writeItemStack(it) }
        if (updateFluid) {
            val fluidStack = fluid
            if (fluidStack == null) {
                buf.writeBoolean(false)
            } else {
                buf.writeBoolean(true)
                buf.writeString(fluidStack.fluid.name)
                buf.writeVarInt(fluidStack.amount)
                buf.writeCompoundTag(fluidStack.tag)
            }
        }
        compostLevel.ifPresent { buf.writeVarInt(it) }
        color.ifPresent { buf.writeInt(it) }
    }

    class Handler : IMessageHandler<MessageUpdateBarrel, IMessage> {
        override fun onMessage(msg: MessageUpdateBarrel, ctx: MessageContext): IMessage? {
            val mc = Minecraft.getMinecraft()

            mc.addScheduledTask {
                if (!mc.world.isBlockLoaded(msg.pos)) return@addScheduledTask

                val barrel = mc.world.getTileEntity(msg.pos)

                if (barrel is BarrelTileEntity) {
                    msg.barrelState.ifPresent { barrel.state = BarrelStates.getState(it) ?: return@ifPresent }
                    msg.item.ifPresent { barrel.item = it }
                    if (msg.updateFluid) barrel.fluid = msg.fluid

                    msg.compostLevel.ifPresent { barrel.compostAmount = it }
                    msg.color.ifPresent { barrel.color = Color(it, true) }
                }
            }

            return null
        }
    }

    companion object {
        const val barrelStateMask: Byte = 0b1
        const val itemMask: Byte = 0b10
        const val fluidMask: Byte = 0b100
        const val compostLevelMask: Byte = 0b1000
        const val colorMask: Byte = 0b10000
    }
}