package exnihiloomnia.crafting.recipes;

import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CrookRecipes {
	public static void register()
	{
		//Crook!
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.CROOK_WOOD, 1, 0),
                        "xx",
                        " x",
                        " x",
                        'x', "stickWood"));

		//BONE Crook!
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.CROOK_BONE, 1, 0),
                        "xx",
                        " x",
                        " x",
                        'x', Items.BONE));
	}
}
