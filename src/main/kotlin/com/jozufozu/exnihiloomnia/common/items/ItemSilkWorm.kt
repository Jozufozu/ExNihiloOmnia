package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.blocks.leaves.BlockSilkwormInfested
import com.jozufozu.exnihiloomnia.common.lib.LibItems
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemSilkWorm : ItemBase(LibItems.SILKWORM) {
    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!worldIn.isRemote) {
            val uninfestedState = worldIn.getBlockState(pos)

            BlockSilkwormInfested.blockMappings[uninfestedState.block]?.let {
                worldIn.setBlockState(pos, it.uninfestedToInfested(uninfestedState))

                val soundType = uninfestedState.block.getSoundType(uninfestedState, worldIn, pos, player)
                worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, soundType.getVolume() * 0.3f, 0.3f * worldIn.rand.nextFloat() + 1.5f)

                player.swingArm(hand)

                if (!player.isCreative) {
                    player.getHeldItem(hand).shrink(1)
                }

                return EnumActionResult.SUCCESS
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ)
    }
}