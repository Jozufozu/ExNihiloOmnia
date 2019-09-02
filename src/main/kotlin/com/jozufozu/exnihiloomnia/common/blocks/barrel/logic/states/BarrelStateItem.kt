package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BarrelStateItem : BarrelState(BarrelStates.ID_ITEMS) {

    override fun addCollisionBoxToList(barrel: TileEntityBarrel, state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?) {
        val block = Block.getBlockFromItem(barrel.item.item)

        if (block === Blocks.AIR) return

        val state = block.getStateFromMeta(barrel.item.metadata)

        val boxes = mutableListOf<AxisAlignedBB>()

        block.addCollisionBoxToList(state, worldIn, BlockPos.ORIGIN, infiniteBoundingBox, boxes, entityIn, false)

        boxes.map {
            val sizeX = it.maxX - it.minX
            val sizeY = it.maxY - it.minY
            val sizeZ = it.maxZ - it.minZ

            val newSizeX = sizeX * 12.0 / 16.0
            val newSizeY = sizeY * 14.0 / 16.0
            val newSizeZ = sizeZ * 12.0 / 16.0

            AxisAlignedBB(  it.minX + 2.0 / 16.0, it.minY + 1.0 / 16.0, it.minZ + 2.0 / 16.0,
                            it.minX + 2.0 / 16.0 + newSizeX, it.minY + 1.0 / 16.0 + newSizeY, it.minZ + 2.0 / 16.0 + newSizeZ)
        }.forEach { addCollisionBoxToList(pos, entityBox, collidingBoxes, it) }
    }

    override fun canInteractWithFluids(barrel: TileEntityBarrel): Boolean {
        return false
    }

    override fun canExtractItems(barrel: TileEntityBarrel): Boolean {
        return true
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        drawContents(barrel, x, y, z, partialTicks)
    }
}
