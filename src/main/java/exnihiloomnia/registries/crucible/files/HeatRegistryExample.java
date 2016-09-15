package exnihiloomnia.registries.crucible.files;

import exnihiloomnia.registries.crucible.pojos.HeatValue;
import exnihiloomnia.registries.crucible.pojos.HeatValueList;

public abstract class HeatRegistryExample {
	
	public static HeatValueList getExampleRecipeList() {
		HeatValueList example = new HeatValueList();
		example.getRecipes().add(new HeatValue("minecraft:furnace_lit", 12));
		example.getRecipes().add(new HeatValue("minecraft:fire", 20));

		return example;
	}
}
