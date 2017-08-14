package com.jozufozu.exnihiloomnia.common.registries;

import com.jozufozu.exnihiloomnia.common.registries.recipes.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class RegistryManager
{
    public static ReloadableRegistry<Ore> ORES;
    
    public static ReloadableRegistry<CompostRecipe> COMPOST;
    public static ReloadableRegistry<FluidCraftingRecipe> FLUID_CRAFTING;
    public static ReloadableRegistry<FluidMixingRecipe> FLUID_MIXING;
    public static ReloadableRegistry<FermentingRecipe> FERMENTING;
    
    public static ReloadableRegistry<SieveRecipe> SIFTING;
    public static ReloadableRegistry<HammerRecipe> HAMMERING;
    
    public static ReloadableRegistry<MeltingRecipe> MELTING;
    public static ReloadableRegistry<HeatSource> HEAT;
    
    public static NonNullList<ItemStack> getHammerRewards(World world, ItemStack hammer, EntityPlayer player, IBlockState toHammer)
    {
        NonNullList<ItemStack> drops = NonNullList.create();
        
        if (toHammer.getBlock() == Blocks.AIR)
            return drops;
    
        for (HammerRecipe hammerRecipe : HAMMERING)
        {
            if (hammerRecipe.matches(toHammer))
            {
                drops.addAll(hammerRecipe.getRewards().roll(player, hammer, world.rand));
            }
        }
        
        return drops;
    }
    
    /**
     * Whether or not the given input can generate rewards
     */
    public static boolean siftable(ItemStack input)
    {
        for (SieveRecipe recipe : SIFTING)
        {
            if (recipe.matches(input))
                return true;
        }
        
        return false;
    }
    
    /**
     * Whether or not the given block can be hammered
     */
    public static boolean hammerable(IBlockState input)
    {
        for (HammerRecipe recipe : HAMMERING)
        {
            if (recipe.matches(input))
                return true;
        }
        
        return false;
    }
    
    public static CompostRecipe getCompost(ItemStack input)
    {
        for (CompostRecipe recipe : COMPOST)
        {
            if (recipe.matches(input))
                return recipe;
        }
        
        return null;
    }
    
    public static FluidMixingRecipe getMixing(FluidStack in, FluidStack on)
    {
        for (FluidMixingRecipe recipe : FLUID_MIXING)
        {
            if (recipe.matches(in, on))
                return recipe;
        }
    
        return null;
    }
    
    public static MeltingRecipe getMelting(ItemStack input)
    {
        for (MeltingRecipe recipe : MELTING)
        {
            if (recipe.matches(input))
                return recipe;
        }
        
        return null;
    }
    
    public static int getHeat(IBlockState source)
    {
        for (HeatSource heatSource : HEAT)
        {
            if (heatSource.matches(source))
                return heatSource.getHeatLevel();
        }
        
        return 0;
    }
}
