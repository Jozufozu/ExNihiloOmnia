package exnihiloomnia.compatibility.jei.categories;

import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JEICrucibleRecipe implements IRecipeWrapper {
    private final List<ItemStack> input = new ArrayList<ItemStack>();
    private final List<FluidStack> output = new ArrayList<FluidStack>();
    private final CrucibleRegistryEntry entry;

    public JEICrucibleRecipe(CrucibleRegistryEntry entry) {
        input.add(0, new ItemStack(entry.getBlock()));
        output.add(new FluidStack(entry.getFluid(), entry.getFluidVolume()));

        this.entry = entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutputs(FluidStack.class, output);
    }

    public CrucibleRegistryEntry getEntry() {
        return entry;
    }

    @Override
    public List<ItemStack> getInputs() {
        return input;
    }

    @Override
    public List getOutputs() {
        return null;
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return output;
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
