package com.jozufozu.exnihiloomnia.common.blocks.leaves

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

class TileEntitySilkwormInfested : TileEntity(), ITickable {
    companion object {
        private const val infestMax: Int = 20 * 50

        private val chunkUpdates: MutableMap<ChunkPos, Long> = hashMapOf()
    }

    var infestTimer: Int = 0
        private set

    val infested: Boolean get() = infestTimer >= infestMax
    val percentInfested: Float get() = infestTimer.toFloat() / infestMax.toFloat()

    override fun update() {
        if (infestTimer <= infestMax) {
            infestTimer++

            if (infestTimer % 20 == 0 || infestTimer == infestMax) {
                markDirty()
            }
            maybeDoRenderUpdate()
        }

        if (!world.isRemote && percentInfested > 0.7 && world.totalWorldTime % 20 == 0L) {
            for (pos in BlockPos.MutableBlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
                if (world.rand.nextFloat() < 0.01 && pos != this.pos) {
                    val uninfested = world.getBlockState(pos)

                    if (uninfested.block in BlockSilkwormInfested.blockMappings) {
                        val infested = BlockSilkwormInfested.blockMappings[uninfested.block] ?: continue

                        world.setBlockState(pos, infested.uninfestedToInfested(uninfested))

                        (world.getTileEntity(pos) as? TileEntitySilkwormInfested)?.infestTimer = (infestMax * (world.rand.nextFloat() * 0.25f + 0.1f)).toInt()

                        break
                    }
                }
            }
        }
    }

    private fun maybeDoRenderUpdate() {
        val chunkPos = ChunkPos(pos)

        if (infested) chunkUpdates.remove(chunkPos)

        if (chunkUpdates.containsKey(chunkPos)) {
            val i = chunkUpdates[chunkPos] ?: 0

            if (world.totalWorldTime - i > 20) {
                doRenderUpdate(chunkPos)
            }
        } else {
            doRenderUpdate(chunkPos)
        }
    }

    private fun doRenderUpdate(chunkPos: ChunkPos) {
        world.markBlockRangeForRenderUpdate(pos, pos)
        chunkUpdates[chunkPos] = world.totalWorldTime
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setInteger("infestTimer", infestTimer)

        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)

        infestTimer = compound.getInteger("infestTimer")
    }

    override fun getUpdateTag(): NBTTagCompound {
        val updateTag = super.getUpdateTag()
        updateTag.setInteger("infestTimer", infestTimer)
        return updateTag
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, 0, updateTag)
    }
}
