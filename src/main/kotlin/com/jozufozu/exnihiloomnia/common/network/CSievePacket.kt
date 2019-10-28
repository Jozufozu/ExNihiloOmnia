package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class CSievePacket {
    val pos: BlockPos

    var mesh: ItemStack? = null

    private var contents: ItemStack? = null
    private var requiredTime: Int = 0

    var countdown: Int? = null
    var workTimer: Int? = null

    constructor(pos: BlockPos) {
        this.pos = pos
    }

    constructor(buf: PacketBuffer): this(buf.readBlockPos()) {
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

    fun sendContents(stack: ItemStack, requiredTime: Int) {
        contents = stack
        this.requiredTime = requiredTime
    }

    fun sendContents() {
        contents = ItemStack.EMPTY
    }

    fun encode(buf: PacketBuffer) {

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

    fun handle(ctx: Supplier<NetworkEvent.Context>) {
        val mc = Minecraft.getInstance()

        ctx.get().enqueueWork {
            if (!mc.world.isBlockLoaded(pos)) return@enqueueWork

            (mc.world.getTileEntity(pos) as? TileEntitySieve)?.let { sieve ->
                mesh?.let { sieve.mesh = it }
                contents?.let {
                    sieve.contents = it
                    sieve.requiredTime = requiredTime
                }

                countdown?.let { sieve.countdown = it }
                workTimer?.let { sieve.workTimer = it }
            }
        }

        ctx.get().packetHandled = true
    }

    companion object {
        const val meshMask: Int = 0b1
        const val contentsMask: Int = 0b10
        const val countdownMask: Int = 0b100
        const val workTimerMask: Int = 0b1000
    }
}