package com.jozufozu.exnihiloomnia.common.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateContainer

class OreBlock(): ExNihiloBlock() {
    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        super.fillStateContainer(builder)
    }
}