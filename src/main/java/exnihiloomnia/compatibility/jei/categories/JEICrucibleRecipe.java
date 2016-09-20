package exnihiloomnia.compatibility.jei.categories;

import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
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

    public JEICrucibleRecipe(CrucibleRegistryEntry entry) {
        input.add(new ItemStack(entry.getBlock()));
        output.add(new FluidStack(entry.getFluid(), (int)entry.getRatio()));
    }

    @Override
    public List getInputs() {
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
