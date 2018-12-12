package com.jozufozu.exnihiloomnia.integration.jei;

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe;
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe;
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe;
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe;
import com.jozufozu.exnihiloomnia.integration.jei.composting.CompostRecipeCategory;
import com.jozufozu.exnihiloomnia.integration.jei.composting.CompostRecipeWrapper;
import com.jozufozu.exnihiloomnia.integration.jei.hammer.HammerRecipeCategory;
import com.jozufozu.exnihiloomnia.integration.jei.hammer.HammerRecipeWrapper;
import com.jozufozu.exnihiloomnia.integration.jei.melting.MeltingRecipeCategory;
import com.jozufozu.exnihiloomnia.integration.jei.melting.MeltingRecipeWrapper;
import com.jozufozu.exnihiloomnia.integration.jei.sieve.SieveRecipeCategory;
import com.jozufozu.exnihiloomnia.integration.jei.sieve.SieveRecipeWrapper;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JEIPlugin
public class JeiPlugin implements IModPlugin
{
    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry)
    {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        
        registry.addRecipeCategories(new HammerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new SieveRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CompostRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MeltingRecipeCategory(guiHelper));
    }
    
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.handleRecipes(HammerRecipe.class, HammerRecipeWrapper::new, HammerRecipeCategory.UID);
        
        registry.handleRecipes(SieveRecipe.class, SieveRecipeWrapper::new, SieveRecipeCategory.UID);
        registry.handleRecipes(CompostRecipe.class, CompostRecipeWrapper::new, CompostRecipeCategory.UID);
        registry.handleRecipes(MeltingRecipe.class, MeltingRecipeWrapper::new, MeltingRecipeCategory.UID);
        
        registry.addRecipes(RegistryManager.SIFTING.getValues(), SieveRecipeCategory.UID);
        registry.addRecipes(RegistryManager.COMPOST.getValues(), CompostRecipeCategory.UID);
        registry.addRecipes(RegistryManager.MELTING.getValues(), MeltingRecipeCategory.UID);
        registry.addRecipes(RegistryManager.HAMMERING.getValues(), HammerRecipeCategory.UID);
    
        registry.addRecipeCatalyst(new ItemStack(ExNihiloItems.DIAMOND_HAMMER), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ExNihiloItems.GOLD_HAMMER), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ExNihiloItems.IRON_HAMMER), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ExNihiloItems.STONE_HAMMER), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ExNihiloItems.WOOD_HAMMER), HammerRecipeCategory.UID);
        
        for (int i = 0; i < BlockPlanks.EnumType.values().length; i++)
        {
            registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.SIEVE, 1, i), SieveRecipeCategory.UID);
        }
    
        registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_GLASS), CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_STONE), CompostRecipeCategory.UID);
        for (int i = 0; i < BlockPlanks.EnumType.values().length; i++)
        {
            registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_WOOD, 1, i), CompostRecipeCategory.UID);
        }
    
        for (int i = 0; i < EnumDyeColor.values().length; i++)
        {
            registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_CONCRETE, 1, i), CompostRecipeCategory.UID);
            registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_TERRACOTTA, 1, i), CompostRecipeCategory.UID);
            registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.BARREL_STAINED_GLASS, 1, i), CompostRecipeCategory.UID);
        }
    
        registry.addRecipeCatalyst(new ItemStack(ExNihiloBlocks.CRUCIBLE), MeltingRecipeCategory.UID);
    }
    
    public static List<List<ItemStack>> getRewardsOutput(Collection<ItemStack> rewards, int slots)
    {
        List<ItemStack> drops = new ArrayList<>(rewards);
        List<List<ItemStack>> out = new ArrayList<>(slots);
    
        for (int i = 0; i < slots; i++)
        {
            out.add(new ArrayList<>());
        }
        
        for (int i = 0; i < drops.size(); i++)
        {
            out.get(i % slots).add(drops.get(i));
        }
        
        return out;
    }
}
