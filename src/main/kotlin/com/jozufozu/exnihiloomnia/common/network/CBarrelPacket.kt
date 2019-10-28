package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier
import kotlin.experimental.and
import kotlin.experimental.or

class CBarrelPacket {
    val pos: BlockPos
    var barrelState: String? = null
    var item: ItemStack? = null
    var fluid: FluidStack? = null
    var compostLevel: Int? = null
    var color: Int? = null

    constructor(pos: BlockPos) {
        this.pos = pos
    }

    constructor(buf: PacketBuffer): this(buf.readBlockPos()) {
        val mask = buf.readByte()

        if ((mask and barrelStateMask) != 0.toByte()) barrelState = buf.readString(64)
        if ((mask and itemMask) != 0.toByte()) item = buf.readItemStack()
        if ((mask and fluidMask) != 0.toByte()) {
            if (buf.readBoolean()) {
                val fluidName = buf.readString(64)
                val amount = buf.readVarInt()
                val tag = buf.readCompoundTag()

                fluid = FluidStack(ForgeRegistries.FLUIDS.getValue(ResourceLocation(fluidName)), amount, tag)
            }
        }
        if ((mask and compostLevelMask) != 0.toByte()) compostLevel = buf.readVarInt()
        if ((mask and colorMask) != 0.toByte()) color = buf.readInt()
    }

    fun encode(buf: PacketBuffer) {

        var mask: Byte = 0
        if (barrelState != null) mask = mask or barrelStateMask
        if (item != null) mask = mask or itemMask
        if (fluid != null) mask = mask or fluidMask
        if (compostLevel != null) mask = mask or compostLevelMask
        if (color != null) mask = mask or colorMask

        buf.writeBlockPos(pos)
        buf.writeByte(mask.toInt())

        barrelState?.let { buf.writeString(it) }
        item?.let { buf.writeItemStack(it) }
        fluid?.let {
            if (it.isEmpty) {
                buf.writeBoolean(false)
            } else {
                buf.writeBoolean(true)
                buf.writeString(it.fluid.registryName.toString())
                buf.writeVarInt(it.amount)
                buf.writeCompoundTag(it.tag)
            }
        }
        compostLevel?.let { buf.writeVarInt(it) }
        color?.let { buf.writeInt(it) }
    }

    fun handle(ctx: Supplier<NetworkEvent.Context>) {
        val mc = Minecraft.getInstance()

        ctx.get().enqueueWork {
            if (!mc.world.isBlockLoaded(pos)) return@enqueueWork

            val barrel = mc.world.getTileEntity(pos)

            if (barrel is BarrelTileEntity) {
                barrelState?.let { barrel.state = BarrelStates.getState(it) ?: return@let }
                item?.let { barrel.item = it }
                fluid?.let { barrel.fluid = it }
                compostLevel?.let { barrel.compostAmount = it }
                color?.let { barrel.color = Color(it, true) }
            }
        }
        ctx.get().packetHandled = true
    }

    companion object {
        const val barrelStateMask: Byte = 0b1
        const val itemMask: Byte = 0b10
        const val fluidMask: Byte = 0b100
        const val compostLevelMask: Byte = 0b1000
        const val colorMask: Byte = 0b10000
    }
}