package exnihiloomnia.crafting.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GrassDrops {
	
	public static void register() {
		MinecraftForge.addGrassSeed(new ItemStack(Items.PUMPKIN_SEEDS, 1), 1);
		MinecraftForge.addGrassSeed(new ItemStack(Items.MELON_SEEDS, 1), 1);
	}
}
