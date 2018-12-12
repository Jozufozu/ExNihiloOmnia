package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks

class BlockInfestedLeaves : BlockBase(LibBlocks.INFESTED_LEAVES, Material.LEAVES, SoundType.PLANT), ITileEntityProvider {

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityInfestedLeaves()
}
