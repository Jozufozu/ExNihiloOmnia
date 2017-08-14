package com.jozufozu.exnihiloomnia.integration.jei.hammer;

import com.google.common.collect.Lists;
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop;
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe;
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HammerRecipeWrapper implements IRecipeWrapper
{
    public final HammerRecipe recipe;
    public final Map<ItemStack, List<WeightedDrop>> rewards;
    
    public HammerRecipeWrapper(HammerRecipe recipe)
    {
        this.recipe = recipe;
        this.rewards = recipe.getRewards().getEquivalencies();
    }
    
    @Override
    public void getIngredients(IIngredients iIngredients)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
    
        inputs.add(Lists.newArrayList(recipe.getIngredient().getMatchingStacks()));
        iIngredients.setInputLists(ItemStack.class, inputs);
        
        iIngredients.setOutputLists(ItemStack.class, JeiPlugin.getRewardsOutput(rewards.keySet(), 9));
    }
}
