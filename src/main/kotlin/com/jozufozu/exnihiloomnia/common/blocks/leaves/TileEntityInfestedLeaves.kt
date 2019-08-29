package com.jozufozu.exnihiloomnia.common.blocks.leaves

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable

class TileEntityInfestedLeaves : TileEntity(), ITickable {
    companion object {
        private const val infestMax: Int = 20 * 30
    }

    var mimic: IBlockState = Blocks.LEAVES.defaultState
    var infestTimer: Int = 0
        private set

    override fun update() {
        if (infestTimer < infestMax) {
            infestTimer++
        }
    }
}
