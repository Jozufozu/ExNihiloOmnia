package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.lib.LibItems
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World

class ItemStone : ModItem(LibItems.SMALL_STONE) {

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)

        if (!playerIn.capabilities.isCreativeMode) stack.shrink(1)

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f))

        if (!worldIn.isRemote) {
            val thrownStone = EntityThrownStone(worldIn, playerIn)
            thrownStone.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f)
            worldIn.spawnEntity(thrownStone)
        }

        StatList.getObjectUseStats(this)?.let { playerIn.addStat(it) }
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}
