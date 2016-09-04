package exnihiloomnia.crafting.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;

public class CrucibleRecipes {
	public static void register()
	{
		GameRegistry.addRecipe(new ItemStack(ENOBlocks.CRUCIBLE_RAW, 1),
				"x x",
				"x x",
				"xxx",
				'x', new ItemStack(ENOItems.PORCELAIN, 1));
		
		GameRegistry.addSmelting(ENOBlocks.CRUCIBLE_RAW, new ItemStack(ENOBlocks.CRUCIBLE), 0.0f);
	}
}
