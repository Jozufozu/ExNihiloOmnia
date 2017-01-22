package exnihiloomnia.registries.hammering;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.hammering.files.HammerRecipeLoader;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class HammerRegistry implements IRegistry<HammerRegistryEntry> {
	private static HashMap<String, HammerRegistryEntry> entries;

	public static final HammerRegistry INSTANCE = new HammerRegistry();
	public static final String path = ENO.path + File.separator + "registries" + File.separator + "hammer" + File.separator;

	public void initialize() {
		entries = new HashMap<>();
		
		if (ENORegistries.loadHammerDefaults) {
			registerVanillaRecipes();
			if (ENORegistries.dumpRegistries)
				HammerRecipeLoader.dumpRecipes(entries, path);
		}
		
		List<HammerRegistryEntry> loaded = HammerRecipeLoader.load(path);
	
		if (loaded != null && !loaded.isEmpty()) {
			for (HammerRegistryEntry entry : loaded) {
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
		entries = new HashMap<>();
	}

	public HashMap<String, HammerRegistryEntry> getEntries() {
		return entries;
	}
	
	public static void add(HammerRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getKey(), entry);
		}
	}
	
	public static void remove(HammerRegistryEntry entry) {
		entries.remove(entry.getKey());
	}
	
	public static boolean isHammerable(IBlockState state) {
		return getEntryForBlockState(state) != null;
	}
	
	public static HammerRegistryEntry getEntryForBlockState(IBlockState state) {
		HammerRegistryEntry entry = entries.get(Block.REGISTRY.getNameForObject(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state));
		
		if (entry != null) {
			return entry;
		}
		else {
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock())  + ":*");
		}
	}
	
	public static void registerVanillaRecipes() {
		for (Ore ore : OreRegistry.registry.values()) {
			ItemStack oreCrushed = new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata());
			ItemStack orePowdered = new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata());

			Block block = ore.getBlock();
			if (ore.hasGravel()) {
				HammerRegistryEntry gravel = new HammerRegistryEntry(block.getStateFromMeta(EnumOreBlockType.GRAVEL.ordinal()), EnumMetadataBehavior.SPECIFIC);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 50, 2);
				gravel.addReward(oreCrushed, 5, 1);
				add(gravel);
			}

			if (ore.hasNether()) {
				HammerRegistryEntry nether = new HammerRegistryEntry(block.getStateFromMeta(EnumOreBlockType.GRAVEL_NETHER.ordinal()), EnumMetadataBehavior.SPECIFIC);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 50, 2);
				nether.addReward(oreCrushed, 5, 1);
				add(nether);
			}

			if (ore.hasEnd()) {
				HammerRegistryEntry nether = new HammerRegistryEntry(block.getStateFromMeta(EnumOreBlockType.GRAVEL_ENDER.ordinal()), EnumMetadataBehavior.SPECIFIC);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 50, 2);
				nether.addReward(oreCrushed, 5, 1);
				add(nether);
			}

			HammerRegistryEntry sand = new HammerRegistryEntry(block.getStateFromMeta(EnumOreBlockType.SAND.ordinal()), EnumMetadataBehavior.SPECIFIC);
			sand.addReward(orePowdered, 100, 0);
			sand.addReward(orePowdered, 100, 0);
			sand.addReward(orePowdered, 100, 0);
			sand.addReward(orePowdered, 100, 0);
			sand.addReward(orePowdered, 50, 2);
			sand.addReward(orePowdered, 5, 1);
			add(sand);
		}

		HammerRegistryEntry stone = new HammerRegistryEntry(Blocks.STONE.getDefaultState(), EnumMetadataBehavior.SPECIFIC);
		ItemStack smallStone = new ItemStack(ENOItems.STONE);
		stone.addReward(smallStone, 100, 0);
		stone.addReward(smallStone, 100, 0);
		stone.addReward(smallStone, 100, 0);
		stone.addReward(smallStone, 50, 2);
		stone.addReward(smallStone, 50, 1);
		add(stone);
		
		HammerRegistryEntry cobble = new HammerRegistryEntry(Blocks.COBBLESTONE.getDefaultState(), EnumMetadataBehavior.SPECIFIC);
		cobble.addReward(new ItemStack(Blocks.GRAVEL), 100, 0);
		add(cobble);
		
		HammerRegistryEntry gravel = new HammerRegistryEntry(Blocks.GRAVEL.getDefaultState(), EnumMetadataBehavior.SPECIFIC);
		gravel.addReward(new ItemStack(Blocks.SAND), 100, 0);
		add(gravel);
		
		HammerRegistryEntry sand = new HammerRegistryEntry(Blocks.SAND.getDefaultState(), EnumMetadataBehavior.SPECIFIC);
		sand.addReward(new ItemStack(ENOBlocks.DUST), 100, 0);
		add(sand);

		HammerRegistryEntry sandstone = new HammerRegistryEntry(Blocks.SANDSTONE.getDefaultState(), EnumMetadataBehavior.IGNORED);
		sandstone.addReward(new ItemStack(Blocks.SAND), 100, 0);
		add(sandstone);

		ItemStack cobblestone = new ItemStack(Blocks.COBBLESTONE);
		HammerRegistryEntry furnace = new HammerRegistryEntry(Blocks.FURNACE.getDefaultState(), EnumMetadataBehavior.IGNORED);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 100, 0);
		furnace.addReward(cobblestone, 50, 1);
		furnace.addReward(cobblestone, 50, 1);
		add(furnace);
		
		HammerRegistryEntry furnace_lit = new HammerRegistryEntry(Blocks.LIT_FURNACE.getDefaultState(), EnumMetadataBehavior.IGNORED);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 100, 0);
		furnace_lit.addReward(cobblestone, 50, 1);
		furnace_lit.addReward(cobblestone, 50, 1);
		add(furnace);

		HammerRegistryEntry nether_gravel = new HammerRegistryEntry(Blocks.NETHERRACK.getDefaultState(), EnumMetadataBehavior.IGNORED);
		nether_gravel.addReward(new ItemStack(ENOBlocks.GRAVEL_NETHER), 100, 0);
		add(nether_gravel);

		HammerRegistryEntry ender_gravel = new HammerRegistryEntry(Blocks.END_STONE.getDefaultState(), EnumMetadataBehavior.IGNORED);
		ender_gravel.addReward(new ItemStack(ENOBlocks.GRAVEL_ENDER), 100, 0);
		add(ender_gravel);
	}
}
