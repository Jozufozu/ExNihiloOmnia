package exnihiloomnia.compatibility.jei.categories.compost;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CompostRecipeHandler implements IRecipeHandler<JEICompostRecipe> {
    @Nonnull
    @Override
    public Class<JEICompostRecipe> getRecipeClass() {
        return JEICompostRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return CompostRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEICompostRecipe recipe) {
        return CompostRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEICompostRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEICompostRecipe recipe) {
        return true;
    }
}
