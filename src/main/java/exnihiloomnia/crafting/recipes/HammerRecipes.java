package exnihiloomnia.crafting.recipes;

import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class HammerRecipes {
	
	public static void register() {
		//Hammers!
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.HAMMER_WOOD, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', "plankWood",
                        'y', "stickWood"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.HAMMER_STONE, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Blocks.COBBLESTONE,
                        'y', "stickWood"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.HAMMER_IRON, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.IRON_INGOT,
                        'y', "stickWood"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.HAMMER_GOLD, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.GOLD_INGOT,
                        'y', "stickWood"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.HAMMER_DIAMOND, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.DIAMOND,
                        'y', "stickWood"));
	}
}
