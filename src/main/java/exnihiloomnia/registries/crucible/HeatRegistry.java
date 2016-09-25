package exnihiloomnia.registries.crucible;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.crucible.files.HeatRegistryLoader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class HeatRegistry implements IRegistry<HeatRegistryEntry> {
	public static HashMap<String, HeatRegistryEntry> entries;

    public static HeatRegistry INSTANCE = new HeatRegistry();
	
	public void initialize() {
		entries = new HashMap<String, HeatRegistryEntry>();

		if (ENORegistries.loadHeatDefaults)
			registerVanillaHeatSources();

		List<HeatRegistryEntry> loaded = HeatRegistryLoader.load(ENO.path + File.separator + "registries" + File.separator + "heat" + File.separator);

		if (loaded != null && !loaded.isEmpty()) {
			for (HeatRegistryEntry entry : loaded) {
				add(entry);
			}
		}
	}

    @Override
    public HashMap<String, HeatRegistryEntry> getEntries() {
        return entries;
    }

    public void clear() {
        entries = new HashMap<String, HeatRegistryEntry>();
    }

	public void add(HeatRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getBlock() + ":" + entry.getMeta(), entry);
		}
	}
	
	public static void register(Block block, int meta, int value) {
		HeatRegistryEntry entry = new HeatRegistryEntry(block, meta, value);
		entries.put(block + ":" + meta, entry);
	}
	
	public static void register(Block block, int value) {
		for(int x = 0; x <= 15; x++) {
			register(block, x, value);
		}
	}
	
	public static boolean containsItem(Block block, int meta) {
		return entries.containsKey(block + ":" + meta);
	}
	
	public static HeatRegistryEntry getItem(Block block, int meta) {
		return entries.get(block + ":" + meta);
	}
	
	public static void registerVanillaHeatSources() {
		register(Blocks.TORCH, 5);
		register(Blocks.LAVA, 16);
		register(Blocks.FLOWING_LAVA, 12);
		register(Blocks.LIT_FURNACE, 12);
		register(Blocks.FIRE, 20);
		register(Blocks.field_189877_df, 20);
	}
}
