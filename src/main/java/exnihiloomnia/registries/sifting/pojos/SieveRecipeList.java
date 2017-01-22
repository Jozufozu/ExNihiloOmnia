package exnihiloomnia.registries.sifting.pojos;

import java.util.ArrayList;

public class SieveRecipeList {
	private ArrayList<SieveRecipe> recipes = new ArrayList<>();

	public ArrayList<SieveRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<SieveRecipe> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(SieveRecipe recipe) {
		this.recipes.add(recipe);
	}
}
