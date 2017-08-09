package com.jozufozu.exnihiloomnia.integration.jei.hammer;

import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop;
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient;
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
    public final WorldIngredient input;
    public final Map<ItemStack, List<WeightedDrop>> rewards;
    
    public HammerRecipeWrapper(HammerRecipe recipe)
    {
        this.input = recipe.getBlock();
        this.rewards = recipe.getRewards().getEquivalencies();
    }
    
    @Override
    public void getIngredients(IIngredients iIngredients)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        //inputs.add(Lists.newArrayList(input.getMatchingStacks()));
        iIngredients.setInputLists(ItemStack.class, inputs);
        
        iIngredients.setOutputLists(ItemStack.class, JeiPlugin.getRewardsOutput(rewards.keySet(), 9));
    }
}
