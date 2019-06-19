package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World

class ItemStone : ItemBase(ItemsLib.SMALL_STONE, Properties()) {

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)

        if (!playerIn.isCreative) stack.shrink(1)

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f))

        if (!worldIn.isRemote) {
            val thrownStone = EntityThrownStone(worldIn, playerIn)
            thrownStone.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f)
            worldIn.spawnEntity(thrownStone)
        }

        StatList.getObjectUseStats(this)?.let { playerIn.addStat(it) }
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}
