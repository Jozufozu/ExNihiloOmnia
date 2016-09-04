package exnihiloomnia.crafting.recipes;

import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class DollRecipes {
	public static void register()
	{
		//Dolls
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.PORCELAIN_DOLL, 1),
                        "xdx",
                        " x ",
                        "x x",
                        'x', ENOItems.PORCELAIN,
                        'd', "gemDiamond"));

        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(ENOItems.PORCELAIN_DOLL, 1),
                        "xdx",
                        " x ",
                        "x x",
                        'x', ENOItems.PORCELAIN,
                        'd', "gemEmerald"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.BLAZE_DOLL, 1),
                        "bwb",
                        "gdg",
                        "brb",
						'd', ENOItems.PORCELAIN_DOLL,
                        'b', Items.BLAZE_POWDER,
                        'w', Items.NETHER_WART,
						'g', "dustGlowstone",
						'r', "dustRedstone"));

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ENOItems.END_DOLL, 1),
						"bwb",
						"gdg",
						"brb",
						'd', ENOItems.PORCELAIN_DOLL,
						'b', "dyeBlack",
						'w', Items.NETHER_WART,
						'g', new ItemStack(Items.DYE, 1, 4),
						'r', "dustRedstone"));
	}
}
