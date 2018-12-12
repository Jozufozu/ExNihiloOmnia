package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.lib.LibItems
import com.jozufozu.exnihiloomnia.common.world.SpawnIsland

class ItemJadeAstrolabe : ItemBase(LibItems.ASTROLABE) {

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (!worldIn.isRemote) {
            val position = playerIn.position

            val radius = 16

            val island = SpawnIsland.createFromWorld(worldIn, position.add(-radius, -radius, -radius), position.add(radius, radius, radius), position)
            island.save(playerIn)
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}
