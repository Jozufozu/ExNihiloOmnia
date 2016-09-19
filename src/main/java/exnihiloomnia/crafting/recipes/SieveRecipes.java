package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class SieveRecipes {
	
	public static void register() {
		//wood sieves
		for (int i = 0; i < 6; i++) {
			if (!ENOConfig.classic_sieve)
				GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(ENOBlocks.SIEVE_WOOD, 1, i),
						"x x",
						"xxx",
						"y y",
						'x', new ItemStack(Blocks.PLANKS, 1, i),
						'y', "stickWood"));
			else
				GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(ENOBlocks.SIEVE_WOOD, 1, i),
						"xzx",
						"xzx",
						"y y",
						'x', new ItemStack(Blocks.PLANKS, 1, i),
						'y', "stickWood",
						'z', new ItemStack(ENOItems.MESH_SILK_WHITE)));
		}
		
		//meshes
		//wood
		if (!ENOConfig.classic_sieve)
			GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(ENOItems.MESH_WOOD, 1),
					"xxx",
					"xxx",
					"xxx",
					'x', "stickWood"));
		
		//silk (white)
		GameRegistry.addRecipe(
			new ShapedOreRecipe(new ItemStack(ENOItems.MESH_SILK_WHITE, 1),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(Items.STRING, 1)));

		//sifters
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(ENOItems.SIFTER_DIAMOND),
						"ddd",
						"sps",
                        "srs",
						'd', "gemDiamond",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON)));
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(ENOItems.SIFTER_GOLD),
						"ddd",
						"sps",
                        "srs",
						'd', "ingotGold",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON)));
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(ENOItems.SIFTER_IRON),
						"ddd",
						"sps",
                        "srs",
						'd', "ingotIron",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON)));
	}
}
