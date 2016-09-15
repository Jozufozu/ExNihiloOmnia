package exnihiloomnia.registries.hammering.files;

import exnihiloomnia.registries.hammering.pojos.HammerRecipe;
import exnihiloomnia.registries.hammering.pojos.HammerRecipeList;
import exnihiloomnia.registries.hammering.pojos.HammerRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;

public abstract class HammerRecipeExample {
	
	public static HammerRecipeList getExampleRecipeList() {
		HammerRecipeList example = new HammerRecipeList();
		
		HammerRecipe coal = new HammerRecipe("minecraft:coal_block", 0, EnumMetadataBehavior.IGNORED);
		coal.getRewards().add(new HammerRecipeReward("minecraft:coal", 0, 9, 100, 0));
		example.getRecipes().add(coal);
		
		HammerRecipe oak = new HammerRecipe("minecraft:log", 0, EnumMetadataBehavior.SPECIFIC);
		oak.getRewards().add(new HammerRecipeReward("minecraft:stick", 0, 7, 100, 0));
		oak.getRewards().add(new HammerRecipeReward("minecraft:stick", 0, 1, 75, 5));
		oak.getRewards().add(new HammerRecipeReward("minecraft:stick", 0, 1, 25, 10));
		example.getRecipes().add(oak);
		
		return example;
	}
}
