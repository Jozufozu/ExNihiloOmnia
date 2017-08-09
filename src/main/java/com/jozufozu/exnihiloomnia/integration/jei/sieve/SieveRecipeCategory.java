package com.jozufozu.exnihiloomnia.integration.jei.sieve;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class SieveRecipeCategory implements IRecipeCategory<SieveRecipeWrapper>
{
    public static final String UID = "exnihiloomnia.sieve";
    
    private static final ResourceLocation texture = new ResourceLocation(ExNihilo.MODID, "textures/gui/jei_sieve.png");
    private static IDrawable background;
    private static IDrawable highlight;
    
    public SieveRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(texture, 0, 0, 166, 146);
        highlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
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
        return I18n.format("jei.exnihiloomnia.sieve");
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
    public void setRecipe(IRecipeLayout recipeLayout, SieveRecipeWrapper sieveRecipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        
        itemStacks.init(0, true, 74, 9);
        
        final int outputSlots = 1;
    
        for (int slotNumber = 0; slotNumber < 45; slotNumber++)
        {
            final int slotX = 2 + (slotNumber % 9 * 18);
            final int slotY = 52 + (slotNumber / 9 * 18);
        
            itemStacks.init(outputSlots + slotNumber, false, slotX, slotY);
        }
        
        itemStacks.set(ingredients);
        
        itemStacks.addTooltipCallback((slot, isInput, stack, toolTip) ->
        {
            if (isInput)
                return;
            
            List<WeightedDrop> dropList = sieveRecipeWrapper.rewards.get(stack);
            for (WeightedDrop weightedDrop : dropList)
            {
                float percentChance = weightedDrop.getChance() * 100;
                int count = weightedDrop.getDrop().getCount();
    
                String type = weightedDrop.getType();
                String translationKey = "exnihiloomnia.drops.type." + type;
                String translate = I18n.format(translationKey);
                
                if (translate.equals(translationKey))
                    translate = type;
                
                String drop = String.format("%.1f%% x%s Type: %s", percentChance, count, translate);
                
                toolTip.add(drop);
            }
        });
    }
}
