package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.BlockRenderLayer

class BlockBarrelGlass : BlockBarrel(LibBlocks.GLASS_BARREL, Material.GLASS, SoundType.GLASS) {
    init {
        this.setHardness(0.7f)
    }

    override fun getBlockLayer() = BlockRenderLayer.TRANSLUCENT
}
