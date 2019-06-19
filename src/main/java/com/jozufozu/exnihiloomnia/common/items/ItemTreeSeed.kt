package com.jozufozu.exnihiloomnia.common.items

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemTreeSeed(
        private val place: Block,
        resourceLocation: ResourceLocation,
        properties: Properties
) : ItemBase(resourceLocation, properties) {

    override fun onItemUse(context: ItemUseContext): ActionResultType {
        val pos = context.pos
        val worldIn = context.world
        val state = worldIn.getBlockState(pos)
        val block = state.block
        val player = context.player

        val blockItemUseContext = BlockItemUseContext(context)
        if (!state.isReplaceable(blockItemUseContext)) pos = pos.offset(facing)

        val stack = context.item

        BlockItem

        return if (!stack.isEmpty && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(Blocks.SAPLING, pos, false, facing, null)) {
            var placeState = place.getStateForPlacement(blockItemUseContext)

            if (this.placeBlockAt(stack, player, worldIn, pos, placeState)) {
                placeState = worldIn.getBlockState(pos)
                val soundType = placeState.block.getSoundType(placeState, worldIn, pos, player)
                worldIn.playSound(player, pos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f)
                stack.shrink(1)
            }

            EnumActionResult.SUCCESS
        } else {
            EnumActionResult.FAIL
        }
    }

    private fun placeBlockAt(stack: ItemStack, player: PlayerEntity, world: World, pos: BlockPos, newState: BlockState): Boolean {
        if (!world.setBlockState(pos, newState, 11)) return false

        val state = world.getBlockState(pos)
        if (state.block === place) {
            place.onBlockPlacedBy(world, pos, state, player, stack)

            if (player is ServerPlayerEntity)
                CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, stack)
        }

        return true
    }
}
