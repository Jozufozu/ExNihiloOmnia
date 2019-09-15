package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockDust : BlockBaseFalling(LibBlocks.DUST, Material.SAND, SoundType.SNOW) {
    init {
        setHardness(0.4f)
    }

    override fun onEndFalling(worldIn: World, pos: BlockPos, p_176502_3_: IBlockState, replace: IBlockState) {
        if (replace.material.isLiquid) {
            worldIn.setBlockState(pos, Blocks.CLAY.defaultState, 3)
        }
    }

    private fun tryTouchWater(worldIn: World, pos: BlockPos): Boolean {
        for (facing in EnumFacing.values()) {
            if (facing != EnumFacing.DOWN) {
                val checkPos = pos.offset(facing)

                if (worldIn.getBlockState(checkPos).material === Material.WATER) {
                    worldIn.setBlockState(pos, Blocks.CLAY.defaultState, 3)
                    return true
                }
            }
        }

        return false
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!this.tryTouchWater(worldIn, pos)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos)
        }
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (!this.tryTouchWater(worldIn, pos)) {
            super.onBlockAdded(worldIn, pos, state)
        }
    }
}