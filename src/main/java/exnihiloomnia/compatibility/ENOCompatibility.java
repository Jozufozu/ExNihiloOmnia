package exnihiloomnia.compatibility;

import exnihiloomnia.compatibility.appliedenergistics.AE2;
import exnihiloomnia.compatibility.forestry.ForestryCompatibility;
import exnihiloomnia.compatibility.industrialcraft.IC2;
import exnihiloomnia.compatibility.tconstruct.TinkersCompatibility;
import exnihiloomnia.compatibility.top.TOPCompatibility;
import exnihiloomnia.compatibility.veinminer.VeinMinerCompatibility;
import exnihiloomnia.compatibility.waila.WailaCompatibility;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ENOCompatibility {
	private static final String CATEGORY_COMPAT_OPTIONS = "compatibility options";
	public static boolean register_veinminer_tools;
	public static boolean register_veinminer_recipes_crook;
	public static boolean register_veinminer_recipes_hammer;
	public static boolean add_smeltery_melting;
	public static boolean add_tcon_modifiers;

	public static void configure(Configuration config) {
		register_veinminer_tools = config.get(CATEGORY_COMPAT_OPTIONS, "VeinMiner: register tools", true).getBoolean(true);
		register_veinminer_recipes_crook = config.get(CATEGORY_COMPAT_OPTIONS, "VeinMiner: whitelist blocks for crook", true).getBoolean(true);
		register_veinminer_recipes_hammer = config.get(CATEGORY_COMPAT_OPTIONS, "VeinMiner: whitelist blocks for hammer", true).getBoolean(true);
		add_smeltery_melting = config.get(CATEGORY_COMPAT_OPTIONS, "TConstruct: add smelting recipes for Ex Nihilo ores", true).getBoolean(true);
		add_tcon_modifiers = config.get(CATEGORY_COMPAT_OPTIONS, "TConstruct: add Hammered and Crook'd modifiers", true).getBoolean(true);
	}

	public static void preInit() {
		if (Loader.isModLoaded("IC2"))
			IC2.registerItems();
		if (Loader.isModLoaded("theoneprobe"))
			TOPCompatibility.register();
		if (Loader.isModLoaded("forestry"))
			ForestryCompatibility.preInit();
	}
	
	public static void init() {
		if (Loader.isModLoaded("Waila"))
			WailaCompatibility.initialize();

		if (Loader.isModLoaded("VeinMiner"))
			VeinMinerCompatibility.initialize();

		if (Loader.isModLoaded("tconstruct"))
			TinkersCompatibility.initilize();

		if (Loader.isModLoaded("appliedenergistics2"))
			AE2.initialize();
	}
}
