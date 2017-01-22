
package exnihiloomnia.compatibility.jei.categories.barrel;

import exnihiloomnia.ENO;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrelCraftingRecipeCategory implements IRecipeCategory<JEIBarrelCraftingRecipe> {

    public static final String UID = "exnihiloomnia:barrel";
    private static final ResourceLocation texture = new ResourceLocation(ENO.MODID, "textures/gui/jei_barrel_crafting.png");

    private final IDrawableStatic background;
    private final IDrawableStatic cover;

    public BarrelCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 128, 64);
        this.cover = guiHelper.createDrawable(texture, 128, 0, 18, 20);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei." + UID);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final JEIBarrelCraftingRecipe recipeWrapper) {

        recipeLayout.getItemStacks().init(0, true, 23, 23);
        recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));

        recipeLayout.getFluidStacks().init(0, true, 55, 23, 18, 20, 1000, false, cover);
        recipeLayout.getFluidStacks().set(0, recipeWrapper.fluid);

        recipeLayout.getItemStacks().init(1, true, 87, 23);
        recipeLayout.getItemStacks().set(1, (ItemStack) recipeWrapper.getOutputs().get(0));
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull JEIBarrelCraftingRecipe recipeWrapper, @Nonnull IIngredients ingredients) {

        recipeLayout.getItemStacks().init(0, true, 23, 23);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

        recipeLayout.getFluidStacks().init(0, true, 55, 23, 18, 20, 1000, false, cover);
        recipeLayout.getFluidStacks().set(ingredients);

        recipeLayout.getItemStacks().init(1, true, 87, 23);
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
