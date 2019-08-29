package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.world.World

class BlockInfestedLeaves : BlockBase(LibBlocks.INFESTED_LEAVES, Material.LEAVES, SoundType.PLANT), ITileEntityProvider {

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityInfestedLeaves()
}
