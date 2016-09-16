package exnihiloomnia.registries.sifting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.sifting.files.SieveRecipeLoader;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import exnihiloomnia.util.enums.EnumOre;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public class SieveRegistry {
	private static HashMap<String, SieveRegistryEntry> entries;
	private static List<ItemStack> EMPTY_REWARDS_ARRAY = new ArrayList<ItemStack>(){};

	public static void initialize() {
		entries = new HashMap<String, SieveRegistryEntry>();
		
		if (ENORegistries.loadSieveDefaults)
			registerVanillaRecipes();
		
		List<SieveRegistryEntry> loaded = SieveRecipeLoader.load(ENO.path + File.separator + "registries" + File.separator + "sieve" + File.separator);
	
		if (loaded != null && !loaded.isEmpty()) {
			for (SieveRegistryEntry entry : loaded) {
				if (entry.getRewards().size() > 0) {
					add(entry);
				}
				else {
					remove(entry);
				}
			}
		}
	}
	
	public static HashMap<String, SieveRegistryEntry> getEntryMap() {
		return entries;
	}
	
	public static void add(SieveRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getKey(), entry);
		}
	}
	
	public static void remove(SieveRegistryEntry entry) {
		entries.remove(entry.getKey());
	}
	
	public static boolean isSiftable(IBlockState state) {
		return getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC) != null || getEntryForBlockState(state, EnumMetadataBehavior.IGNORED) != null;
	}
	
	public static List<ItemStack> generateRewards(IBlockState state) {
		if (state == null || state.getBlock().equals(Blocks.AIR)) {
			return EMPTY_REWARDS_ARRAY;
		}
		else {
			List<ItemStack> rewards = new ArrayList<ItemStack>();
			
			SieveRegistryEntry specific = getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC);
			SieveRegistryEntry generic = getEntryForBlockState(state, EnumMetadataBehavior.IGNORED);
			
			if (specific == null && generic == null)
				return EMPTY_REWARDS_ARRAY;
			
			if (specific != null) {
				for (SieveReward r : specific.getRewards()) {
					ItemStack i = r.generateReward();
					
					if (i != null)
						rewards.add(i);
				}
			}
				
			if (generic != null) {
				for (SieveReward r : generic.getRewards()) {
					ItemStack i = r.generateReward();
					
					if (i != null)
						rewards.add(i);
				}
			}
			
			return rewards;
		}
	}
	
	public static SieveRegistryEntry getEntryForBlockState(IBlockState state, EnumMetadataBehavior behavior) {
		if (behavior == EnumMetadataBehavior.SPECIFIC) {
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state));
		}
		else {
			return entries.get(Block.REGISTRY.getNameForObject(state.getBlock())  + ":*");
		}
	}
	
	public static void registerVanillaRecipes() {
		SieveRegistryEntry dirt = new SieveRegistryEntry(Blocks.DIRT.getDefaultState(), EnumMetadataBehavior.IGNORED);
		dirt.addReward(new ItemStack(ENOItems.STONE, 1), 100);
		dirt.addReward(new ItemStack(ENOItems.STONE, 1), 100);
		dirt.addReward(new ItemStack(ENOItems.STONE, 1), 75);
		dirt.addReward(new ItemStack(ENOItems.STONE, 1), 50);
		dirt.addReward(new ItemStack(Items.WHEAT_SEEDS, 1), 7);
		dirt.addReward(new ItemStack(ENOItems.GRASS_SEEDS, 1), 7);
		dirt.addReward(new ItemStack(Items.MELON_SEEDS, 1), 3);
		dirt.addReward(new ItemStack(Items.PUMPKIN_SEEDS, 1), 3);
		dirt.addReward(new ItemStack(Items.BEETROOT_SEEDS, 1), 3);
		dirt.addReward(new ItemStack(ENOItems.SUGARCANE_SEEDS, 1), 3);
		dirt.addReward(new ItemStack(ENOItems.POTATO_SEEDS, 1), 7);
		dirt.addReward(new ItemStack(ENOItems.CARROT_SEEDS, 1), 7);
		dirt.addReward(new ItemStack(ENOItems.SEED_ACACIA), 2);
		dirt.addReward(new ItemStack(ENOItems.SEED_BIRCH), 2);
		dirt.addReward(new ItemStack(ENOItems.SEED_DARK_OAK), 2);
		dirt.addReward(new ItemStack(ENOItems.SEED_JUNGLE), 2);
		dirt.addReward(new ItemStack(ENOItems.SEED_OAK), 5);
		dirt.addReward(new ItemStack(ENOItems.SEED_SPRUCE), 2);
		add(dirt);
		
		SieveRegistryEntry gravel = new SieveRegistryEntry(Blocks.GRAVEL.getDefaultState(), EnumMetadataBehavior.IGNORED);
        
		for (EnumOre ore : ENO.oreList) {
        	if (ore.hasGravel())
            	gravel.addReward(new ItemStack(ENOItems.BROKEN_ORE, 1, ore.getMetadata()), ore.getRarity());
        }
        
		gravel.addReward(new ItemStack(Items.FLINT, 1), 25);
		gravel.addReward(new ItemStack(Items.COAL, 1), 13);
		gravel.addReward(new ItemStack(Items.DYE, 1, 4), 5); //Lapis
		gravel.addReward(new ItemStack(Items.DYE, 1, 4), 7); //Lapis
		gravel.addReward(new ItemStack(Items.EMERALD, 1), 1);
		gravel.addReward(new ItemStack(Items.EMERALD, 1), 1);
		gravel.addReward(new ItemStack(Items.DIAMOND, 1), 1);
		gravel.addReward(new ItemStack(Items.DIAMOND, 1), 1);
		add(gravel);

		SieveRegistryEntry nethergravel = new SieveRegistryEntry(ENOBlocks.GRAVEL_NETHER.getDefaultState(), EnumMetadataBehavior.IGNORED);
		
		for (EnumOre ore : ENO.oreList) {
			if (ore.hasNether())
				nethergravel.addReward(new ItemStack(ENOItems.BROKEN_ORE_NETHER, 1, ore.getMetadata()), ore.getRarity());
		}
		
		nethergravel.addReward(new ItemStack(Items.GHAST_TEAR), 7);
		nethergravel.addReward(new ItemStack(Items.GLOWSTONE_DUST), 3);
		add(nethergravel);

		SieveRegistryEntry endergravel = new SieveRegistryEntry(ENOBlocks.GRAVEL_ENDER.getDefaultState(), EnumMetadataBehavior.IGNORED);
		
		for (EnumOre ore : ENO.oreList) {
			if (ore.hasEnd())
				endergravel.addReward(new ItemStack(ENOItems.BROKEN_ORE_ENDER, 1, ore.getMetadata()), ore.getRarity());
		}
		
		add(endergravel);

        SieveRegistryEntry sand = new SieveRegistryEntry(Blocks.SAND.getDefaultState(), EnumMetadataBehavior.IGNORED);
		
        for (EnumOre ore : ENO.oreList) {
			if (ore.hasGravel())
				sand.addReward(new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata()), ore.getRarity());
		}
		
        sand.addReward(new ItemStack(Items.DYE, 1, 3), 3); //Cocoa beans
        sand.addReward(new ItemStack(ENOItems.CACTUS_SEEDS, 1), 3);
        sand.addReward(new ItemStack(ENOItems.SPORES, 1), 1);
        add(sand);

        SieveRegistryEntry dust = new SieveRegistryEntry(ENOBlocks.DUST.getDefaultState(), EnumMetadataBehavior.IGNORED);
        
        for (EnumOre ore : ENO.oreList) {
			if (ore.hasGravel())
            	dust.addReward(new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata()), ore.getRarity());
        }
		
        dust.addReward(new ItemStack(Items.DYE, 1, 15), 20); //Bonemeal
        dust.addReward(new ItemStack(Items.GUNPOWDER, 1), 7);
        dust.addReward(new ItemStack(Items.BLAZE_POWDER, 1), 5);
        dust.addReward(new ItemStack(Items.REDSTONE, 1), 13);
        dust.addReward(new ItemStack(Items.GLOWSTONE_DUST, 1), 6);
        add(dust);

		SieveRegistryEntry soulsand = new SieveRegistryEntry(Blocks.SOUL_SAND.getDefaultState(), EnumMetadataBehavior.IGNORED);
		soulsand.addReward(new ItemStack(Items.QUARTZ, 1), 100);
		soulsand.addReward(new ItemStack(Items.NETHER_WART, 1), 6);
		soulsand.addReward(new ItemStack(Items.GHAST_TEAR, 1), 1);
		add(soulsand);
	}
}
