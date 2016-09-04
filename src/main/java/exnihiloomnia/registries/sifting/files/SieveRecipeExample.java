package exnihiloomnia.registries.sifting.files;

import exnihiloomnia.registries.sifting.pojos.SieveRecipe;
import exnihiloomnia.registries.sifting.pojos.SieveRecipeList;
import exnihiloomnia.registries.sifting.pojos.SieveRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;

public abstract class SieveRecipeExample {
	public static SieveRecipeList getExampleRecipeList()
	{
		SieveRecipeList example = new SieveRecipeList();
		
		SieveRecipe gravel = new SieveRecipe("minecraft:gravel", 0, EnumMetadataBehavior.IGNORED);
		gravel.getRewards().add(new SieveRecipeReward("minecraft:flint", 0, 1, 100));
		example.getRecipes().add(gravel);
		
		SieveRecipe oakleaves = new SieveRecipe("minecraft:leaves", 0, EnumMetadataBehavior.SPECIFIC);
		oakleaves.getRewards().add(new SieveRecipeReward("minecraft:sapling", 0, 1, 50));
		example.getRecipes().add(oakleaves);
		
		return example;
	}
}
