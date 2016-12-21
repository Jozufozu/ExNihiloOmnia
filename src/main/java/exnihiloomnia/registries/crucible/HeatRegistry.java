package exnihiloomnia.registries.crucible;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.crucible.files.HeatRegistryLoader;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class HeatRegistry implements IRegistry<HeatRegistryEntry> {
	public static HashMap<String, HeatRegistryEntry> entries;
	public static String path = ENO.path + File.separator + "registries" + File.separator + "heat" + File.separator;

    public static HeatRegistry INSTANCE = new HeatRegistry();
	
	public void initialize() {
		entries = new HashMap<String, HeatRegistryEntry>();

		if (ENORegistries.loadHeatDefaults) {
			registerVanillaHeatSources();
			if (ENORegistries.dumpRegistries)
				HeatRegistryLoader.dumpRecipes(entries, path);
		}

		List<HeatRegistryEntry> loaded = HeatRegistryLoader.load(path);

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

	public static void add(HeatRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getKey(), entry);
		}
	}
	
	public static boolean hasHeat(IBlockState state) {
		return getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC) != null || getEntryForBlockState(state, EnumMetadataBehavior.IGNORED) != null;
	}

	public static HeatRegistryEntry getEntryForBlockState(IBlockState state, EnumMetadataBehavior behavior) {
		if (behavior == EnumMetadataBehavior.SPECIFIC)
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state));
		else
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock())  + ":*");
	}

	public static int getHeatForState(IBlockState state) {
		HeatRegistryEntry specific = getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC);
		HeatRegistryEntry generic = getEntryForBlockState(state, EnumMetadataBehavior.IGNORED);

		if (specific != null)
			return specific.getHeat();
		else if (generic != null)
			return generic.getHeat();
		else
			return 0;
	}
	
	public static void registerVanillaHeatSources() {
		add(new HeatRegistryEntry(Blocks.TORCH.getDefaultState(), EnumMetadataBehavior.IGNORED, 8));
		add(new HeatRegistryEntry(Blocks.LAVA.getDefaultState(), EnumMetadataBehavior.IGNORED, 16));
		add(new HeatRegistryEntry(Blocks.FLOWING_LAVA.getDefaultState(), EnumMetadataBehavior.IGNORED, 12));
		add(new HeatRegistryEntry(Blocks.LIT_FURNACE.getDefaultState(), EnumMetadataBehavior.IGNORED, 12));
		add(new HeatRegistryEntry(Blocks.FIRE.getDefaultState(), EnumMetadataBehavior.IGNORED, 20));
		add(new HeatRegistryEntry(Blocks.MAGMA.getDefaultState(), EnumMetadataBehavior.IGNORED, 20));
	}
}
