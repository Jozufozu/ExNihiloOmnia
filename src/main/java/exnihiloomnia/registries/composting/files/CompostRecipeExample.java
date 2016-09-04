package exnihiloomnia.registries.composting.files;

import exnihiloomnia.registries.composting.pojos.CompostRecipe;
import exnihiloomnia.registries.composting.pojos.CompostRecipeList;
import exnihiloomnia.util.enums.EnumMetadataBehavior;

public abstract class CompostRecipeExample {
	public static CompostRecipeList getExampleRecipeList()
	{
		CompostRecipeList example = new CompostRecipeList();
		example.getRecipes().add(new CompostRecipe("minecraft:apple", 0, EnumMetadataBehavior.IGNORED, 100, "FFF68F"));
		example.getRecipes().add(new CompostRecipe("minecraft:sapling", 1, EnumMetadataBehavior.SPECIFIC, 125, "2E8042"));
		
		return example;
	}
}
