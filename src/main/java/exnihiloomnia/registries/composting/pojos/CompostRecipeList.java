package exnihiloomnia.registries.composting.pojos;

import java.util.ArrayList;

public class CompostRecipeList {
	private ArrayList<CompostRecipe> recipes = new ArrayList<CompostRecipe>();

	public ArrayList<CompostRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<CompostRecipe> recipes) {
		this.recipes = recipes;
	}
}
