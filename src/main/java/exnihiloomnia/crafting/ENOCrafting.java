package exnihiloomnia.crafting;

import exnihiloomnia.crafting.recipes.*;
import net.minecraftforge.common.config.Configuration;

public class ENOCrafting {
	private static final String CATEGORY_CRAFTING_OPTIONS = "crafting options";
	
	public static boolean barrels_allowed;
	public static boolean crooks_allowed;
	public static boolean crucibles_allowed;
	public static boolean hammers_allowed;
	public static boolean sieves_allowed;
	public static boolean dolls_allowed;
	public static boolean porcelain_buckets_allowed;
	
	public static void configure(Configuration config)
	{
		barrels_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow barrels", true).getBoolean(true);
		crooks_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow crooks", true).getBoolean(true);
		crucibles_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow crucibles", true).getBoolean(true);
		hammers_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow hammers", true).getBoolean(true);
		sieves_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow sieves", true).getBoolean(true);
		dolls_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow dolls", true).getBoolean(true);
		porcelain_buckets_allowed = config.get(CATEGORY_CRAFTING_OPTIONS, "allow porcelain buckets", true).getBoolean(true);
	}

	public static void registerRecipes()
	{
		if (barrels_allowed)
			BarrelRecipes.register();
		
		if (crooks_allowed)
			CrookRecipes.register();
		
		if (crucibles_allowed)
			CrucibleRecipes.register();
		
		if (hammers_allowed)
			HammerRecipes.register();
		
		if (sieves_allowed)
			SieveRecipes.register();

		if (dolls_allowed)
			DollRecipes.register();

		if (porcelain_buckets_allowed)
			BucketRecipes.register();
		
		MiscRecipes.registerSmeltingRecipes();
		MiscRecipes.registerOtherRecipes();

		GrassDrops.register();
	}
}
