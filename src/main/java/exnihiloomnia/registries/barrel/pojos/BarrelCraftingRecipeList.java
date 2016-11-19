package exnihiloomnia.registries.barrel.pojos;

import java.util.ArrayList;

public class BarrelCraftingRecipeList {
	private ArrayList<BarrelCraftingRecipe> recipes = new ArrayList<BarrelCraftingRecipe>();

	public ArrayList<BarrelCraftingRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<BarrelCraftingRecipe> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(BarrelCraftingRecipe recipe) {
		this.recipes.add(recipe);
	}
}
