package com.jozufozu.exnihiloomnia.integration.jei.melting;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.ModConfig;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MeltingRecipeCategory implements IRecipeCategory<MeltingRecipeWrapper>
{
    public static final String UID = "exnihiloomnia.melting";
    
    private static final ResourceLocation texture = new ResourceLocation(ExNihilo.MODID, "textures/gui/jei_crucible.png");
    private static IDrawable background;
    
    public MeltingRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(texture, 0, 0, 40, 66);
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
        return I18n.format("jei.exnihiloomnia.melting");
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
    public void drawExtras(Minecraft minecraft)
    {
    
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MeltingRecipeWrapper meltingRecipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        
        itemStacks.init(0, true, 11, 4);
        fluidStacks.init(0, false, 8, 30, 24, 26, ModConfig.blocks.crucible.fluidCapacity, true, null);
        
        itemStacks.set(ingredients);
        fluidStacks.set(ingredients);
        
        fluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> tooltip.add(I18n.format("jei.exnihiloomnia.info.required_heat", meltingRecipeWrapper.recipe.getRequiredHeat())));
    }
}
