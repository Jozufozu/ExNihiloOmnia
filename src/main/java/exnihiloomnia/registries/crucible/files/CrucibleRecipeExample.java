package exnihiloomnia.registries.crucible.files;

import exnihiloomnia.registries.crucible.pojos.CrucibleRecipe;
import exnihiloomnia.registries.crucible.pojos.CrucibleRegistryList;
import net.minecraftforge.fluids.FluidRegistry;

public abstract class CrucibleRecipeExample {
	public static CrucibleRegistryList getExampleRecipeList()
	{
		CrucibleRegistryList example = new CrucibleRegistryList();
		example.getRecipes().add(new CrucibleRecipe("minecraft:cobblestone", 0, 250, FluidRegistry.getFluidName(FluidRegistry.LAVA), 250));
		example.getRecipes().add(new CrucibleRecipe("minecraft:netherrack", 0, 250, FluidRegistry.getFluidName(FluidRegistry.LAVA), 1000));
		
		return example;
	}
}
