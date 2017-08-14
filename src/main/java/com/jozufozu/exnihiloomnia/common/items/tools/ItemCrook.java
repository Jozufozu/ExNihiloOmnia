package com.jozufozu.exnihiloomnia.common.items.tools;

import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCrook extends ItemBaseTool
{
    public ItemCrook(ResourceLocation registryName, ToolMaterial toolMaterial)
    {
        super(registryName, toolMaterial);
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
    {
        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        
        if (!world.isRemote && state.getMaterial() == Material.LEAVES)
        {
            Block leaves = state.getBlock();
            
            leaves.dropBlockAsItem(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
        }
        
        return false;
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getMaterial() == Material.LEAVES;
    }
    
    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        return state.getMaterial() == Material.LEAVES ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, state);
    }
}
