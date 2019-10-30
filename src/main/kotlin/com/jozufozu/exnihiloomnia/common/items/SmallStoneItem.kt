
package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.entity.ThrownStoneEntity
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.stats.Stats
import net.minecraft.util.*
import net.minecraft.world.World

class SmallStoneItem : ModItem(ItemsLib.SMALL_STONE, Properties()) {

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)

        if (!playerIn.isCreative) stack.shrink(1)

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f))

        if (!worldIn.isRemote) {

            val thrownStone = ThrownStoneEntity(worldIn, playerIn)
            thrownStone.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f)
            worldIn.addEntity(thrownStone)
        }

        playerIn.addStat(Stats.ITEM_USED[this])
        return ActionResult(ActionResultType.SUCCESS, stack)
    }
}