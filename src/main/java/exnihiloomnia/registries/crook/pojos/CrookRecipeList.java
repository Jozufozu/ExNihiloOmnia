package exnihiloomnia.registries.crook.pojos;

import java.util.ArrayList;

public class CrookRecipeList {
	private ArrayList<CrookRecipe> recipes = new ArrayList<>();

	public ArrayList<CrookRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<CrookRecipe> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(CrookRecipe recipe) {
		this.recipes.add(recipe);
	}
}
