package com.jozufozu.exnihiloomnia.integration.jei.sieve;

import com.google.common.collect.Lists;
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop;
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe;
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SieveRecipeWrapper implements IRecipeWrapper
{
    public final Ingredient input;
    public final Map<ItemStack, List<WeightedDrop>> rewards;
    
    public SieveRecipeWrapper(SieveRecipe recipe)
    {
        this.input = recipe.getInput();
        this.rewards = recipe.getOutput().getEquivalencies();
    }
    
    @Override
    public void getIngredients(IIngredients iIngredients)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(Lists.newArrayList(input.getMatchingStacks()));
        iIngredients.setInputLists(ItemStack.class, inputs);
        
        iIngredients.setOutputLists(ItemStack.class, JeiPlugin.getRewardsOutput(rewards.keySet(), 45));
    }
}
