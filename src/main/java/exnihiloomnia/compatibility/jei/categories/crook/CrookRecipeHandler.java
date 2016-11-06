package exnihiloomnia.compatibility.jei.categories.crook;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CrookRecipeHandler implements IRecipeHandler<JEICrookRecipe> {
    @Nonnull
    @Override
    public Class<JEICrookRecipe> getRecipeClass() {
        return JEICrookRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return CrookRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEICrookRecipe recipe) {
        return CrookRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEICrookRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEICrookRecipe recipe) {
        return true;
    }
}
