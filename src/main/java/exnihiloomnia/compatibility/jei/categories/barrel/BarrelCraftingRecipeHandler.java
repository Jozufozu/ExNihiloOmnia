package exnihiloomnia.compatibility.jei.categories.barrel;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class BarrelCraftingRecipeHandler implements IRecipeHandler<JEIBarrelCraftingRecipe> {
    @Nonnull
    @Override
    public Class<JEIBarrelCraftingRecipe> getRecipeClass() {
        return JEIBarrelCraftingRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return BarrelCraftingRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEIBarrelCraftingRecipe recipe) {
        return BarrelCraftingRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEIBarrelCraftingRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEIBarrelCraftingRecipe recipe) {
        return true;
    }
}
