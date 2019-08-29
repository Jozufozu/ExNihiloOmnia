package com.jozufozu.exnihiloomnia.common.items.tools

import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos

class ItemCrook(registryName: ResourceLocation, toolMaterial: ToolMaterial) : ItemBaseTool(registryName, toolMaterial) {

    override fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos, player: EntityPlayer): Boolean {
        val world = player.world
        val state = world.getBlockState(pos)

        if (!world.isRemote && state.material === Material.LEAVES) {
            val leaves = state.block

            leaves.dropBlockAsItem(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack))
        }

        return false
    }

    override fun canHarvestBlock(blockIn: IBlockState): Boolean {
        return blockIn.material === Material.LEAVES
    }

    override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
        return if (state.material === Material.LEAVES) this.efficiencyOnProperMaterial else super.getStrVsBlock(stack, state)
    }
}
