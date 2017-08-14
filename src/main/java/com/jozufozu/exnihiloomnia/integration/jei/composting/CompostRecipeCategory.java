package com.jozufozu.exnihiloomnia.integration.jei.composting;

import com.jozufozu.exnihiloomnia.ExNihilo;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CompostRecipeCategory implements IRecipeCategory<CompostRecipeWrapper>
{
    public static final String UID = "exnihiloomnia.compost";
    
    public static final ResourceLocation compostLoc = new ResourceLocation(ExNihilo.MODID, "textures/blocks/compost.png");
    public static final ResourceLocation texture = new ResourceLocation(ExNihilo.MODID, "textures/gui/jei_barrel.png");
    private static IDrawable background;
    public static IDrawable overlay;
    public static IDrawable compost;
    
    
    public CompostRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(texture, 0, 0, 90, 39);
        overlay = guiHelper.createDrawable(texture, 90, 0, 18, 20);
        compost = guiHelper.createDrawable(compostLoc, 0, 0, 16, 16, 16, 16);
    }
    
    @Nonnull
    @Override
    public String getUid()
    {
        return UID;
    }
    
    @Nonnull
    @Override
    public String getTitle()
    {
        return I18n.format("jei.exnihiloomnia.compost");
    }
    
    @Nonnull
    @Override
    public String getModName()
    {
        return ExNihilo.NAME;
    }
    
    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CompostRecipeWrapper compostRecipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        
        itemStacks.init(0, true, 4, 15);
        
        itemStacks.init(1, false, 68, 15);
        
        itemStacks.set(ingredients);
    }
}
