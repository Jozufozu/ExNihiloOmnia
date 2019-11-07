package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.blocks.leaves.BlockSilkwormInfested
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.SoundCategory

class ItemSilkWorm : ModItem(ItemsLib.SILKWORM) {
    override fun onItemUse(p_195939_1_: ItemUseContext): ActionResultType {
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