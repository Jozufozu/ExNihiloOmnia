package exnihiloomnia.registries;

import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import net.minecraftforge.common.config.Configuration;

public class ENORegistries {
	public static final String CATEGORY_DEFAULT_RECIPES = "default recipes";
	public static final String CATEGORY_ORE_RECIPES = "ex nihilo ore recipes";

	public static boolean loadCompostDefaults = true;
	public static boolean loadHammerDefaults = true;
	public static boolean loadSieveDefaults = true;
	public static boolean loadHeatDefaults = true;
	public static boolean loadCrucibleDefaults = true;

	public static boolean loadSieveOres = true;
	public static boolean loadHammerOres = true;

	public static void configure(Configuration config) {
		loadCompostDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default compost recipes", true).getBoolean(true);
		loadHammerDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default hammer recipes", true).getBoolean(true);
		loadSieveDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default sieve recipes", true).getBoolean(true);
        loadCrucibleDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default melting recipes", true).getBoolean(true);
        loadHeatDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default heat indexes", true).getBoolean(true);

		loadSieveOres = config.get(CATEGORY_ORE_RECIPES, "load ex nihilo ore sieve recipes", true).getBoolean(true);
		loadHammerOres = config.get(CATEGORY_ORE_RECIPES, "load ex nihilo ore hammer recipes", true).getBoolean(true);

		CompostRegistry.INSTANCE.initialize();
		HammerRegistry.INSTANCE.initialize();
		SieveRegistry.INSTANCE.initialize();
		HeatRegistry.INSTANCE.initialize();
		CrucibleRegistry.INSTANCE.initialize();
	}
}
