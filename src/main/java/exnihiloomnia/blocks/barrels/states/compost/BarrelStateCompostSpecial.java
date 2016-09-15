package exnihiloomnia.blocks.barrels.states.compost;

import java.util.ArrayList;

import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BarrelStateCompostSpecial extends BarrelStateCompost {
	private ArrayList<String> ingredients = new ArrayList<String>();
	
	public void addIngredient(ItemStack item, EnumMetadataBehavior behavior) {
		if (item != null && behavior != null) {
			if (behavior == EnumMetadataBehavior.IGNORED) {
				ingredients.add(Item.REGISTRY.getNameForObject(item.getItem()) + ":*");
			}
			
			if (behavior == EnumMetadataBehavior.IGNORED) {
				ingredients.add(Item.REGISTRY.getNameForObject(item.getItem()) + ":" + item.getMetadata());
			}
		}
	}

	public void removeIngredient(ItemStack item) {
		ingredients.remove(Item.REGISTRY.getNameForObject(item.getItem()) + ":*");
		ingredients.remove(Item.REGISTRY.getNameForObject(item.getItem()) + ":" + item.getMetadata());
	}
	
	public boolean isIngredient(ItemStack item) {
		return (ingredients.contains(Item.REGISTRY.getNameForObject(item.getItem()) + ":*") || ingredients.contains(Item.REGISTRY.getNameForObject(item.getItem()) + ":" + item.getMetadata()));
	}
}
