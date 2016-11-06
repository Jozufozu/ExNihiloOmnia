
package exnihiloomnia.compatibility.jei.categories.crucible;

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
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class CrucibleRecipeCategory implements IRecipeCategory<JEICrucibleRecipe> {

    public static final String UID = "exnihiloomnia:crucible";
    private static final ResourceLocation texture = new ResourceLocation(ENO.MODID, "textures/gui/jei_crucible.png");

    private final IDrawableStatic background;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 128, 64);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final JEICrucibleRecipe recipeWrapper) {
        recipeLayout.getItemStacks().init(0, true, 55, 2);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getInputs().get(0));

        recipeLayout.getFluidStacks().init(0, false, 52, 30, 24, 24, 8000, true, null);
        recipeLayout.getFluidStacks().set(0, recipeWrapper.getFluidOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, JEICrucibleRecipe recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 55, 2);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

        recipeLayout.getFluidStacks().init(0, false, 52, 30, 24, 24, 8000, true, null);
        recipeLayout.getFluidStacks().set(0, ingredients.getOutputs(FluidStack.class));
    }
}
