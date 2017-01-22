package exnihiloomnia.blocks.barrels.states.compost;

import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class BarrelStateCompostSpecial extends BarrelStateCompost {
	private final ArrayList<String> ingredients = new ArrayList<>();
	
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
