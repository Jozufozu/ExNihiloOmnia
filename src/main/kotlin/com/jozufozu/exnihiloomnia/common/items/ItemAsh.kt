package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemDye
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemAsh : ModItem(ItemsLib.ASH) {
    override fun onItemUse(player: PlayerEntity, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (ItemDye.applyBonemeal(player.getHeldItem(hand), worldIn, pos, player, hand)) {
            if (!worldIn.isRemote) {
                worldIn.playEvent(2005, pos, 0)
            }

            return EnumActionResult.SUCCESS
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ)
    }
}
