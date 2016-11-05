package exnihiloomnia.registries.crook.files;

import exnihiloomnia.registries.crook.pojos.CrookRecipe;
import exnihiloomnia.registries.crook.pojos.CrookRecipeList;
import exnihiloomnia.registries.crook.pojos.CrookRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;

public abstract class CrookRecipeExample {
	
	public static CrookRecipeList getExampleRecipeList() {
		CrookRecipeList example = new CrookRecipeList();
		
		CrookRecipe coal = new CrookRecipe("minecraft:sapling", 0, EnumMetadataBehavior.SPECIFIC);
		coal.getRewards().add(new CrookRecipeReward("exnihiloomnia:seed_oak", 0, 1, 100, 0));
		example.getRecipes().add(coal);
		
		CrookRecipe oak = new CrookRecipe("minecraft:sapling", 0, EnumMetadataBehavior.IGNORED);
		oak.getRewards().add(new CrookRecipeReward("minecraft:stick", 0, 7, 100, 0));
		oak.getRewards().add(new CrookRecipeReward("minecraft:stick", 0, 1, 75, 5));
		oak.getRewards().add(new CrookRecipeReward("minecraft:stick", 0, 1, 25, 10));
		example.getRecipes().add(oak);
		
		return example;
	}
}
