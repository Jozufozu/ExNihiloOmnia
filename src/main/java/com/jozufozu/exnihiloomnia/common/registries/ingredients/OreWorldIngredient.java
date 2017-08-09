package com.jozufozu.exnihiloomnia.common.registries.ingredients;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.stream.Collectors;

public class OreWorldIngredient extends WorldIngredient
{
    private final List<ItemStack> matches;
    
    public OreWorldIngredient(String oreDictName)
    {
        super(null, 0);
    
        matches = OreDictionary.getOres(oreDictName).stream()
                               .filter(itemStack -> Block.getBlockFromItem(itemStack.getItem()) != Blocks.AIR)
                               .collect(Collectors.toList());
    }
    
    @Override
    public boolean test(IBlockState iBlockState)
    {
        Block block = iBlockState.getBlock();
        ItemStack equivalent = new ItemStack(block, 1, block.getMetaFromState(iBlockState));
    
        for (ItemStack match : matches)
        {
            if (OreDictionary.itemMatches(match, equivalent, false))
                return true;
        }
        
        return false;
    }
}
