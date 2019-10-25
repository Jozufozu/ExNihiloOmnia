package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import com.jozufozu.exnihiloomnia.common.world.SpawnIsland
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemJadeAstrolabe : ModItem(ItemsLib.ASTROLABE) {

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
