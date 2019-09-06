package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessageUpdateSieve() : IMessage {
    constructor(pos: BlockPos): this() {
        this.pos = pos
    }

    lateinit var pos: BlockPos

    var mesh: ItemStack? = null

    private var contents: ItemStack? = null
    private var requiredTime: Int = 0

    var countdown: Int? = null
    var workTimer: Int? = null

    fun sendContents(stack: ItemStack, requiredTime: Int) {
        contents = stack
        this.requiredTime = requiredTime
    }

    fun sendContents() {
        contents = ItemStack.EMPTY
    }

    override fun fromBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)
        pos = buf.readBlockPos()
        val mask = buf.readByte().toInt()

        if ((mask and meshMask) != 0) {
            mesh = buf.readStackWithoutSize()
        }

        if ((mask and contentsMask) != 0) {
            val stack = buf.readStackWithoutSize()

            if (!stack.isEmpty) {
                requiredTime = buf.readVarInt()
            }

            contents = stack
        }

        if ((mask and countdownMask) != 0) countdown = buf.readVarInt()
        if ((mask and workTimerMask) != 0) workTimer = buf.readVarInt()
    }

    override fun toBytes(buf: ByteBuf) {
        val buf = PacketBuffer(buf)

        var mask = 0
        if (mesh != null) mask = mask or meshMask
        if (contents != null) mask = mask or contentsMask
        if (countdown != null) mask = mask or countdownMask
        if (workTimer != null) mask = mask or workTimerMask

        buf.writeBlockPos(pos)
        buf.writeByte(mask)

        mesh?.let { buf.writeStackWithoutSize(it) }
        contents?.let {
            buf.writeStackWithoutSize(it)

            if (!it.isEmpty) {
                buf.writeVarInt(requiredTime)
            }
        }

        countdown?.let { buf.writeVarInt(it) }
        workTimer?.let { buf.writeVarInt(it) }
    }

    class Handler : IMessageHandler<MessageUpdateSieve, IMessage> {
        override fun onMessage(msg: MessageUpdateSieve, ctx: MessageContext): IMessage? {
            val mc = Minecraft.getMinecraft()

            mc.addScheduledTask {
                if (!mc.world.isBlockLoaded(msg.pos)) return@addScheduledTask

                (mc.world.getTileEntity(msg.pos) as? TileEntitySieve)?.let { sieve ->
                    msg.mesh?.let { sieve.mesh = it }
                    msg.contents?.let {
                        sieve.contents = it
                        sieve.requiredTime = msg.requiredTime
                    }

                    msg.countdown?.let { sieve.countdown = it }
                    msg.workTimer?.let { sieve.workTimer = it }
                }
            }

            return null
        }
    }

    companion object {
        const val meshMask: Int = 0b1
        const val contentsMask: Int = 0b10
        const val countdownMask: Int = 0b100
        const val workTimerMask: Int = 0b1000
    }
}