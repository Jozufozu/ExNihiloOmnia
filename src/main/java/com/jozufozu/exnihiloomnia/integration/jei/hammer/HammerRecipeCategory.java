package com.jozufozu.exnihiloomnia.integration.jei.hammer;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class HammerRecipeCategory implements IRecipeCategory<HammerRecipeWrapper>
{
    public static final String UID = "exnihiloomnia.hammer";
    
    private static final ResourceLocation texture = new ResourceLocation(ExNihilo.MODID, "textures/gui/jei_hammer.png");
    private static IDrawable background;
    private static IDrawable highlight;
    
    public HammerRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(texture, 0, 0, 166, 74);
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
        return I18n.format("jei.exnihiloomnia.hammer");
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
    public void setRecipe(IRecipeLayout recipeLayout, HammerRecipeWrapper hammerRecipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        
        itemStacks.init(0, true, 74, 9);
        
        final int outputSlots = 1;
    
        for (int slotNumber = 0; slotNumber < 9; slotNumber++)
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
            
            List<WeightedDrop> dropList = hammerRecipeWrapper.rewards.get(stack);
            for (WeightedDrop weightedDrop : dropList)
            {
                float percentChance = weightedDrop.getChance() * 100;
                int count = weightedDrop.getDrop().getCount();
                
                String drop = I18n.format("jei.exnihiloomnia.info.reward_chance", percentChance, count);
                
                toolTip.add(drop);
            }
        });
    }
}
