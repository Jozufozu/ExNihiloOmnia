package exnihiloomnia.registries.crucible.pojos;

import java.util.ArrayList;

public class CrucibleRegistryList {
	private ArrayList<CrucibleRecipe> recipes = new ArrayList<CrucibleRecipe>();

	public ArrayList<CrucibleRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<CrucibleRecipe> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(CrucibleRecipe recipe) {
		this.recipes.add(recipe);
	}
}
