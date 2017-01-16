package exnihiloomnia.compatibility.enderio;


import crazypants.enderio.machine.recipe.RecipeOutput;
import crazypants.enderio.machine.sagmill.SagMillRecipeManager;
import net.minecraft.item.ItemStack;

public class EnderIOCompatibility {
    public static void addCompatibility(ItemStack input, ItemStack output, ItemStack output2) {
        output.stackSize = 5;
        output2.stackSize = 2;

        SagMillRecipeManager.getInstance().addRecipe(input, 2000, new RecipeOutput(output), new RecipeOutput(output2, .3f));
    }
}
