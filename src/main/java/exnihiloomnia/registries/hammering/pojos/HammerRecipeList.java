package exnihiloomnia.registries.hammering.pojos;

import java.util.ArrayList;

public class HammerRecipeList {
	private ArrayList<HammerRecipe> recipes = new ArrayList<HammerRecipe>();

	public ArrayList<HammerRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<HammerRecipe> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(HammerRecipe recipe) {
		this.recipes.add(recipe);
	}
}
