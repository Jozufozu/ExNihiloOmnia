package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks

class BlockBarrelStainedGlass : BlockBarrelColored(LibBlocks.STAINED_GLASS_BARREL, Material.GLASS, SoundType.GLASS) {
    companion object {
        @JvmField val COLOR = BlockBarrelColored.COLOR
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty(COLOR, EnumDyeColor.WHITE)
        this.setHardness(0.7f)
    }

    override fun getBlockLayer() = BlockRenderLayer.TRANSLUCENT
}
