package exnihiloomnia.compatibility.jei.categories;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CrucibleRecipeHandler implements IRecipeHandler<JEICrucibleRecipe> {
    @Nonnull
    @Override
    public Class<JEICrucibleRecipe> getRecipeClass() {
        return JEICrucibleRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return CrucibleRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEICrucibleRecipe recipe) {
        return CrucibleRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEICrucibleRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEICrucibleRecipe recipe) {
        return true;
    }
}
