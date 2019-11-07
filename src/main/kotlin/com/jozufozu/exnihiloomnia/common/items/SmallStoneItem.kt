
package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.entity.ThrownStoneEntity
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.stats.Stats
import net.minecraft.util.*
import net.minecraft.world.World

class SmallStoneItem : ModItem(ItemsLib.SMALL_STONE, Properties()) {

    override fun onItemRightClick(world: World, player: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(handIn)

        if (!player.isCreative) stack.shrink(1)

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f))

        if (!world.isRemote) {

            val thrownStone = ThrownStoneEntity(player, world)
            thrownStone.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f)
            world.addEntity(thrownStone)
        }

        player.addStat(Stats.ITEM_USED[this])
        return ActionResult(ActionResultType.SUCCESS, stack)
    }
}