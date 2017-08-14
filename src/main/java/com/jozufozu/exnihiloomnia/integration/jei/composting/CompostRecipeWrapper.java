package com.jozufozu.exnihiloomnia.integration.jei.composting;

import com.google.common.collect.Lists;
import com.jozufozu.exnihiloomnia.common.ModConfig;
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe;
import com.jozufozu.exnihiloomnia.common.util.Color;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompostRecipeWrapper implements IRecipeWrapper
{
    public final CompostRecipe recipe;
    
    public CompostRecipeWrapper(CompostRecipe recipe)
    {
        this.recipe = recipe;
    }
    
    @Override
    public void getIngredients(IIngredients iIngredients)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(Lists.newArrayList(recipe.getInput().getMatchingStacks()));
        iIngredients.setInputLists(ItemStack.class, inputs);
        
        iIngredients.setOutput(ItemStack.class, recipe.getOutput());
    }
    
    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        GlStateManager.pushMatrix();
    
        Color color = recipe.getColor();
        GlStateManager.color(color.r, color.g, color.b, color.a);
        
        GlStateManager.translate(37, 16, 0);
        GlStateManager.scale(1, 1.125, 1);
        
        CompostRecipeCategory.compost.draw(minecraft);
        
        GlStateManager.popMatrix();
    
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        CompostRecipeCategory.overlay.draw(minecraft, 36, 15);
    }
    
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        if (mouseX > 36 && mouseY > 15 && mouseX < 54 && mouseY < 35)
        {
            return Lists.newArrayList(I18n.format("jei.exnihiloomnia.info.compost_amount", recipe.getAmount(), ModConfig.blocks.barrel.compostCapacity));
        }
        
        return Collections.emptyList();
    }
}
