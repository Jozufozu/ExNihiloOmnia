package exnihiloomnia.compatibility.actuallyadditions;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.item.ItemStack;

public class AACompatibility {
    public static void addCompatibility(ItemStack input, ItemStack output, ItemStack output2) {
        output.stackSize = 5;
        output2.stackSize = 2;

        ActuallyAdditionsAPI.addCrusherRecipe(input, output, output2, 30);
    }
}
