package exnihiloomnia.crafting.recipes;

import exnihiloomnia.crafting.ENOCrafting;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MiscRecipes {
	
	public static void registerOtherRecipes() {
		//web
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(Blocks.WEB, 1),
						"xxx",
						"xyx",
						"xxx",
						'x', new ItemStack(Items.STRING, 1),
						'y', new ItemStack(Items.SLIME_BALL, 1)));
		
		//cobble from stones, why did I do this as a config
        switch (ENOCrafting.stone_required) {
            case 0: 
            	break;
            
            case 1:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 2:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 3:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 4:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 5:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 6:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 7:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 8:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
                
            case 9:
                GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE),
                    new ItemStack(ENOItems.STONE));
                break;
        }

		//porcelain
		GameRegistry.addShapelessRecipe(new ItemStack(ENOItems.PORCELAIN, 1),
				new ItemStack(Items.CLAY_BALL),
				new ItemStack(ENOItems.ASH),
				new ItemStack(Items.DYE, 1, 15)); //bonemeal
	}
	
	public static void registerSmeltingRecipes() {
		GameRegistry.addSmelting(new ItemStack(Items.STICK, 1), new ItemStack(ENOItems.ASH, 1), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.STONEBRICK, 1, 1), new ItemStack(Blocks.STONEBRICK, 1, 2), 0);
		GameRegistry.addSmelting(new ItemStack(ENOItems.SILKWORM, 1), new ItemStack(ENOItems.COOKED_SILKWORM, 1), 0);
	}
}
