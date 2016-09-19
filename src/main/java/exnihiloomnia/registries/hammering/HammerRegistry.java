package exnihiloomnia.registries.hammering;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.hammering.files.HammerRecipeLoader;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import exnihiloomnia.util.enums.EnumOre;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HammerRegistry {
	private static HashMap<String, HammerRegistryEntry> entries;

	public static void initialize() {
		entries = new HashMap<String, HammerRegistryEntry>();
		
		if (ENORegistries.loadHammerDefaults)
			registerVanillaRecipes();
		
		List<HammerRegistryEntry> loaded = HammerRecipeLoader.load(ENO.path + File.separator + "registries" + File.separator + "hammer" + File.separator);
	
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
	
	public static HashMap<String, HammerRegistryEntry> getEntryMap() {
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
		for (EnumOre ore : ENO.oreList) {
			ItemStack oreCrushed = new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata());
			ItemStack orePowdered = new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata());
			if (ore.hasGravel()) {
				HammerRegistryEntry gravel = new HammerRegistryEntry(ENOBlocks.ORE_GRAVEL.getStateFromMeta(ore.getMetadata()), EnumMetadataBehavior.SPECIFIC);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 100, 0);
				gravel.addReward(oreCrushed, 50, 2);
				gravel.addReward(oreCrushed, 5, 1);
				add(gravel);
			}
			
			if (ore.hasNether()) {
				HammerRegistryEntry nether = new HammerRegistryEntry(ENOBlocks.ORE_GRAVEL_NETHER.getStateFromMeta(ore.getMetadata()), EnumMetadataBehavior.SPECIFIC);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 50, 2);
				nether.addReward(oreCrushed, 5, 1);
				add(nether);
			}
			
			if (ore.hasEnd()) {
				HammerRegistryEntry nether = new HammerRegistryEntry(ENOBlocks.ORE_GRAVEL_ENDER.getStateFromMeta(ore.getMetadata()), EnumMetadataBehavior.SPECIFIC);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 100, 0);
				nether.addReward(oreCrushed, 50, 2);
				nether.addReward(oreCrushed, 5, 1);
				add(nether);
			}

			HammerRegistryEntry sand = new HammerRegistryEntry(ENOBlocks.ORE_SAND.getStateFromMeta(ore.getMetadata()), EnumMetadataBehavior.SPECIFIC);
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
