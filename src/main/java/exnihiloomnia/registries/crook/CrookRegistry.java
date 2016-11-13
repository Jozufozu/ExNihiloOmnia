package exnihiloomnia.registries.crook;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.crook.files.CrookRecipeLoader;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CrookRegistry implements IRegistry<CrookRegistryEntry> {
	private static HashMap<String, CrookRegistryEntry> entries;

	public static CrookRegistry INSTANCE = new CrookRegistry();
	public static String path = ENO.path + File.separator + "registries" + File.separator + "crook" + File.separator;

	public static CrookRegistryEntry SILKWORM;

	public void initialize() {
		SILKWORM = new CrookRegistryEntry(Blocks.LEAVES.getDefaultState(), EnumMetadataBehavior.IGNORED);
		SILKWORM.addReward(new ItemStack(ENOItems.SILKWORM), 1, 1);

		entries = new HashMap<String, CrookRegistryEntry>();
		
		if (ENORegistries.loadCrookDefaults) {
			registerVanillaRecipes();
			CrookRecipeLoader.dumpRecipes(entries, path);
		}
		
		List<CrookRegistryEntry> loaded = CrookRecipeLoader.load(path);
	
		if (loaded != null && !loaded.isEmpty()) {
			for (CrookRegistryEntry entry : loaded) {
				if (entry.getRewards().size() > 0) {
					add(entry);
				}
				else {
					remove(entry);
				}
			}
		}
	}

	@Override
	public void clear() {
		entries = new HashMap<String, CrookRegistryEntry>();
	}

	public HashMap<String, CrookRegistryEntry> getEntries() {
		return entries;
	}
	
	public static void add(CrookRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getKey(), entry);
		}
	}
	
	public static void remove(CrookRegistryEntry entry) {
		entries.remove(entry.getKey());
	}
	
	public static boolean isCrookable(IBlockState state) {
		return getEntryForBlockState(state) != null;
	}
	
	public static CrookRegistryEntry getEntryForBlockState(IBlockState state) {
		CrookRegistryEntry entry = entries.get(Block.REGISTRY.getNameForObject(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state));
		
		if (entry != null) {
			return entry;
		}
		else {
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock())  + ":*");
		}
	}
	
	public static void registerVanillaRecipes() {
		CrookRegistryEntry infestedLeaves = new CrookRegistryEntry(ENOBlocks.INFESTED_LEAVES.getDefaultState(), EnumMetadataBehavior.IGNORED);
		infestedLeaves.addReward(new ItemStack(ENOItems.SILKWORM), (int)(100 * ENOConfig.silkworm_chance), 0);
		add(infestedLeaves);
	}
}
