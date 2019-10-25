package com.jozufozu.exnihiloomnia.common.items.tools

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.init.Enchantments
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos

class ItemCrook(registryName: ResourceLocation, toolMaterial: IItemTier) : ItemBaseTool(registryName, toolMaterial) {

    override fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos, player: PlayerEntity): Boolean {
        val world = player.world
        val state = world.getBlockState(pos)

        if (!world.isRemote && state.material === Material.LEAVES) {
            val leaves = state.block

            leaves.dropBlockAsItem(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack) + 1)

            if (world.rand.nextFloat() < 0.1) Block.spawnAsEntity(world, pos, ItemStack(ExNihiloItems.SILKWORM))
        }

        return false
    }

    override fun canHarvestBlock(blockIn: BlockState): Boolean {
        return blockIn.material === Material.LEAVES
    }

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return if (state.material === Material.LEAVES) this.efficiency else super.getDestroySpeed(stack, state)
    }
}
