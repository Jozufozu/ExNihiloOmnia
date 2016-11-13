package exnihiloomnia.registries.crucible.pojos;

import java.util.ArrayList;

public class HeatValueList {
	private ArrayList<HeatValue> recipes = new ArrayList<HeatValue>();

	public ArrayList<HeatValue> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<HeatValue> recipes) {
		this.recipes = recipes;
	}

	public void addRecipe(HeatValue recipe) {
		this.recipes.add(recipe);
	}
}
