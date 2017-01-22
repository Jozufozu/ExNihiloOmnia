package exnihiloomnia.compatibility.jei.categories.barrel;

import exnihiloomnia.registries.barrel.BarrelCraftingTrigger;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class JEIBarrelCraftingRecipe implements IRecipeWrapper{

    public final FluidStack fluid;
    public final ItemStack input;
    public final ItemStack output;

    private final BarrelCraftingTrigger entry;

    public JEIBarrelCraftingRecipe(BarrelCraftingTrigger entry) {
        this.entry = entry;

        this.fluid = new FluidStack(entry.fluid, 1000);
        this.input = entry.input;
        this.output = entry.output;
    }

    public BarrelCraftingTrigger getEntry() {
        return entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(FluidStack.class, Collections.singletonList(fluid));
        ingredients.setInputs(ItemStack.class, Collections.singletonList(input));

        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public List getInputs() {
        return Collections.singletonList(input);
    }

    @Override
    public List getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return Collections.singletonList(fluid);
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return null;
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
