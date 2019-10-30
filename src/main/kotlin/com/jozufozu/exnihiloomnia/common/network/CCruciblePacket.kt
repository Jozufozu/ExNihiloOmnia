package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.common.blocks.crucible.CrucibleTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

class CCruciblePacket {
    val pos: BlockPos

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

    constructor(pos: BlockPos) {
        this.pos = pos
    }

    constructor(buf: PacketBuffer): this(buf.readBlockPos()) {
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

            fluid = FluidStack(ForgeRegistries.FLUIDS.getValue(ResourceLocation(fluidName)), 0, tag)
        }

        if ((mask and fluidAmountMask) != 0) {
            fluidAmount = buf.readInt()
            partialFluidAmount = buf.readFloat()
        }

        if ((mask and heatMask) != 0) heat = buf.readVarInt()
    }

    fun encode(buf: PacketBuffer) {
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
            buf.writeString(it.fluid.registryName.toString())
            buf.writeCompoundTag(it.tag)
        }
        fluidAmount?.let {
            buf.writeInt(it)
            buf.writeFloat(partialFluidAmount)
        }

        heat?.let { buf.writeVarInt(it) }
    }

    fun handle(ctx: Supplier<NetworkEvent.Context>) {
        val mc = Minecraft.getInstance()

        ctx.get().enqueueWork {
            if (!mc.world.isBlockLoaded(pos)) return@enqueueWork

            (mc.world.getTileEntity(pos) as? CrucibleTileEntity)?.let { crucible ->
                item?.let {
                    crucible.solid = it
                    crucible.requiredHeatLevel = requiredHeat
                    crucible.meltingRatio = meltingRatio
                }
                solidAmount?.let { crucible.solidAmount = it }
                fluid?.let { crucible.fluidHandler.fluid = it }
                fluidAmount?.let {
                    crucible.fluidHandler.fluid?.amount = it
                    crucible.partialFluid = partialFluidAmount
                }
                heat?.let { crucible.currentHeatLevel = it }
            }
        }
        ctx.get().packetHandled = true
    }

    companion object {
        const val itemMask: Int = 0b1
        const val solidAmountMask: Int = 0b10
        const val fluidMask: Int = 0b100
        const val fluidAmountMask: Int = 0b1000
        const val heatMask: Int = 0b10000
    }
}