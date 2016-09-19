package exnihiloomnia.compatibility.jei.categories;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class SieveRecipeHandler implements IRecipeHandler<JEISieveRecipe> {
    @Nonnull
    @Override
    public Class<JEISieveRecipe> getRecipeClass() {
        return JEISieveRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return SieveRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEISieveRecipe recipe) {
        return SieveRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEISieveRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEISieveRecipe recipe) {
        return true;
    }
}
