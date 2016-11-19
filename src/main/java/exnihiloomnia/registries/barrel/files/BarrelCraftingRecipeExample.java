package exnihiloomnia.registries.barrel.files;

import exnihiloomnia.registries.barrel.pojos.BarrelCraftingRecipe;
import exnihiloomnia.registries.barrel.pojos.BarrelCraftingRecipeList;

public abstract class BarrelCraftingRecipeExample {
	
	public static BarrelCraftingRecipeList getExampleRecipeList() {
		BarrelCraftingRecipeList example = new BarrelCraftingRecipeList();
		example.getRecipes().add(new BarrelCraftingRecipe("lava", "minecraft:stick", "minecraft:gunpowder"));
		example.getRecipes().add(new BarrelCraftingRecipe("witchwater", "minecraft:emerald", "minecraft:diamond"));
		
		return example;
	}
}
