package exnihiloomnia.crafting.recipes;

import exnihiloomnia.blocks.ENOBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BarrelRecipes {
	
	public static void register() {
		//wood barrels
		for(int i = 0; i < 6; i++) {
			GameRegistry.addRecipe(new ItemStack(ENOBlocks.BARREL_WOOD, 1, i),
					"x x",
					"x x",
					"xyx",
					'x', new ItemStack(Blocks.PLANKS, 1, i),
					'y', new ItemStack(Blocks.WOODEN_SLAB, 1, i));
		}

		//stone barrel
		GameRegistry.addRecipe(new ItemStack(ENOBlocks.BARREL_STONE, 1, 0),
				"x x",
				"x x",
				"xyx",
				'x', new ItemStack(Blocks.STONE, 1, 0),
				'y', new ItemStack(Blocks.STONE_SLAB, 1, 0));

		//glass barrel
		GameRegistry.addRecipe(new ItemStack(ENOBlocks.BARREL_GLASS, 1, 0),
				"x x",
				"x x",
				"xyx",
				'x', new ItemStack(Blocks.GLASS, 1, 0),
				'y', new ItemStack(Blocks.GLASS_PANE, 1, 0));

		//stained glass barrels
		for(int i = 0; i < 16; i++) {
			GameRegistry.addRecipe(new ItemStack(ENOBlocks.BARREL_GLASS_COLORED, 1, i),
					"x x",
					"x x",
					"xyx",
					'x', new ItemStack(Blocks.STAINED_GLASS, 1, i),
					'y', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, i));
		}
	}
}
