package com.jozufozu.exnihiloomnia.integration.jei.melting;

import com.google.common.collect.Lists;
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class MeltingRecipeWrapper implements IRecipeWrapper
{
    public final MeltingRecipe recipe;
    
    public MeltingRecipeWrapper(MeltingRecipe recipe)
    {
        this.recipe = recipe;
    }
    
    @Override
    public void getIngredients(IIngredients iIngredients)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(Lists.newArrayList(recipe.getInput().getMatchingStacks()));
        iIngredients.setInputLists(ItemStack.class, inputs);
        
        iIngredients.setOutput(FluidStack.class, recipe.getOutput());
    }
}
