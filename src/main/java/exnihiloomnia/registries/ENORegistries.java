package exnihiloomnia.registries;

import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.crook.CrookRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import net.minecraftforge.common.config.Configuration;

public class ENORegistries {
	public static final String CATEGORY_DEFAULT_RECIPES = "default recipes";
	public static final String CATEGORY_ORE_RECIPES = "ex nihilo ore recipes";

	public static boolean dumpRegistries = true;

	public static boolean loadCompostDefaults = true;
	public static boolean loadHammerDefaults = true;
	public static boolean loadCrookDefaults = true;
	public static boolean findLeaves = true;
	public static boolean loadSieveDefaults = true;
	public static boolean loadHeatDefaults = true;
	public static boolean loadCrucibleDefaults = true;

	public static boolean loadSieveOres = true;
	public static boolean loadHammerOres = true;

	public static void configure(Configuration config) {
		dumpRegistries = config.get(CATEGORY_DEFAULT_RECIPES, "transfer all registries to json files", false).getBoolean(false);

		loadCompostDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default compost recipes", true).getBoolean(true);
		loadHammerDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default hammer recipes", true).getBoolean(true);
		loadCrookDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default crook recipes", true).getBoolean(true);
		findLeaves = config.get(CATEGORY_DEFAULT_RECIPES, "find leaves for the crook registry", true).getBoolean(true);
		loadSieveDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default sieve recipes", true).getBoolean(true);
        loadCrucibleDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default melting recipes", true).getBoolean(true);
        loadHeatDefaults = config.get(CATEGORY_DEFAULT_RECIPES, "load default heat indexes", true).getBoolean(true);

		loadSieveOres = config.get(CATEGORY_ORE_RECIPES, "load ex nihilo ore sieve recipes", true).getBoolean(true);
		loadHammerOres = config.get(CATEGORY_ORE_RECIPES, "load ex nihilo ore hammer recipes", true).getBoolean(true);
	}

	public static void initialize() {
		CompostRegistry.INSTANCE.initialize();
		HammerRegistry.INSTANCE.initialize();
		CrookRegistry.INSTANCE.initialize();
		SieveRegistry.INSTANCE.initialize();
		HeatRegistry.INSTANCE.initialize();
		CrucibleRegistry.INSTANCE.initialize();
	}

	public static void clear() {
		CompostRegistry.INSTANCE.clear();
		HammerRegistry.INSTANCE.clear();
		CrookRegistry.INSTANCE.clear();
		SieveRegistry.INSTANCE.clear();
		HeatRegistry.INSTANCE.clear();
		CrucibleRegistry.INSTANCE.clear();
	}
}
