package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItemUseContext
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldReader
import net.minecraft.world.World

class BlockDust : FallingModBlock(BlocksLib.DUST, Properties.create(Material.SNOW_BLOCK).hardnessAndResistance(0.4f)) {

    override fun onEndFalling(worldIn: World, pos: BlockPos, p_176502_3_: BlockState, replace: BlockState) {
        if (replace.material.isLiquid) {
            worldIn.setBlockState(pos, Blocks.CLAY.defaultState, 3)
        }
    }

    private fun tryTouchWater(worldIn: IWorldReader, pos: BlockPos): Boolean {
        for (facing in Direction.values()) {
            if (facing != Direction.DOWN) {
                val checkPos = pos.offset(facing)

                if (worldIn.getBlockState(checkPos).material === Material.WATER) {
                    return true
                }
            }
        }

        return false
    }

    override fun getStateForPlacement(ctx: BlockItemUseContext): BlockState? {
        return if (tryTouchWater(ctx.world, ctx.pos)) Blocks.CLAY.defaultState else super.getStateForPlacement(ctx)
    }
}