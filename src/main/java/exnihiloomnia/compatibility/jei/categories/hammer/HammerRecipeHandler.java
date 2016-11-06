package exnihiloomnia.compatibility.jei.categories.hammer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class HammerRecipeHandler implements IRecipeHandler<JEIHammerRecipe> {
    @Nonnull
    @Override
    public Class<JEIHammerRecipe> getRecipeClass() {
        return JEIHammerRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return HammerRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JEIHammerRecipe recipe) {
        return HammerRecipeCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JEIHammerRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull JEIHammerRecipe recipe) {
        return true;
    }
}
