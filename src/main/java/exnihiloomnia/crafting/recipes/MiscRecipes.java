package exnihiloomnia.crafting.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import exnihiloomnia.items.ENOItems;

public class MiscRecipes {
	public static void registerOtherRecipes()
	{

		//web
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(Blocks.WEB, 1),
						"xxx",
						"xyx",
						"xxx",
						'x', new ItemStack(Items.STRING, 1),
						'y', new ItemStack(Items.SLIME_BALL, 1)));
		
		//cobble from stones
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(Blocks.COBBLESTONE, 1),
						"xx",
						"xx",
						'x', new ItemStack(ENOItems.STONE, 1)));
		
		//porcelain
		GameRegistry.addShapelessRecipe(new ItemStack(ENOItems.PORCELAIN, 1),
				new ItemStack(Items.CLAY_BALL, 1),
				new ItemStack(ENOItems.ASH),
				new ItemStack(Items.DYE, 1, 15)); //bonemeal
	}
	
	public static void registerSmeltingRecipes()
	{
		GameRegistry.addSmelting(new ItemStack(Items.STICK, 1), new ItemStack(ENOItems.ASH, 1), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.STONEBRICK, 1, 1), new ItemStack(Blocks.STONEBRICK, 1, 2), 0);
		GameRegistry.addSmelting(new ItemStack(ENOItems.SILKWORM, 1), new ItemStack(ENOItems.COOKED_SILKWORM, 1), 0);
	}
}
