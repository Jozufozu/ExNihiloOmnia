package exnihiloomnia.registries.composting;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.composting.files.CompostRecipeLoader;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CompostRegistry implements IRegistry<CompostRegistryEntry> {
	private static HashMap<String, CompostRegistryEntry> entries;
	public static final String path = ENO.path + File.separator + "registries" + File.separator + "compost" + File.separator;

	public static final CompostRegistry INSTANCE = new CompostRegistry();
	
	public void initialize() {
		entries = new HashMap<>();
		
		if (ENORegistries.loadCompostDefaults) {
			registerVanillaRecipes();
			if (ENORegistries.dumpRegistries)
				CompostRecipeLoader.dumpRecipes(entries, path);
		}
		
		List<CompostRegistryEntry> loaded = CompostRecipeLoader.load(path);
	
		if (loaded != null && !loaded.isEmpty()) {
			for (CompostRegistryEntry entry : loaded) {
				if (entry.getVolume() > 0) {
					add(entry);
				}
				else {
					remove(entry);
				}
			}
		}
	}
	
	public HashMap<String, CompostRegistryEntry> getEntries() {
		return entries;
	}

	public void clear() {
		entries = new HashMap<>();
	}
	
	public static void add(CompostRegistryEntry entry) {
		if (entry != null) {
			entries.put(entry.getKey(), entry);
		}
	}
	
	public static void remove(CompostRegistryEntry entry) {
		entries.remove(entry.getKey());
	}
	
	public static boolean isCompostable(ItemStack item) {
		return getEntryForItemStack(item) != null;
	}
	
	public static CompostRegistryEntry getEntryForItemStack(ItemStack item) {
		CompostRegistryEntry entry = entries.get(Item.REGISTRY.getNameForObject(item.getItem()) + ":" + item.getMetadata());
		
		if (entry != null) {
			return entry;
		}
		else {
			return entries.get(Item.REGISTRY.getNameForObject(item.getItem()) + ":*");
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	public static void registerVanillaRecipes() {
		//saplings
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 0), 125, new Color("35A82A"), EnumMetadataBehavior.SPECIFIC)); //oak
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 1), 125, new Color("2E8042"), EnumMetadataBehavior.SPECIFIC)); //spruce
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 2), 125, new Color("6CC449"), EnumMetadataBehavior.SPECIFIC)); //birch
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 3), 125, new Color("22A116"), EnumMetadataBehavior.SPECIFIC)); //jungle
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 4), 125, new Color("B8C754"), EnumMetadataBehavior.SPECIFIC)); //acacia
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 5), 125, new Color("378030"), EnumMetadataBehavior.SPECIFIC)); //dark_oak

		//leaves
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES), 1, 0), 125, new Color("35A82A"), EnumMetadataBehavior.SPECIFIC)); //oak
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES), 1, 1), 125, new Color("2E8042"), EnumMetadataBehavior.SPECIFIC)); //spruce
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES), 1, 2), 125, new Color("6CC449"), EnumMetadataBehavior.SPECIFIC)); //birch
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES), 1, 3), 125, new Color("22A116"), EnumMetadataBehavior.SPECIFIC)); //jungle
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES2), 1, 0), 125, new Color("B8C754"), EnumMetadataBehavior.SPECIFIC)); //acacia
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LEAVES2), 1, 1), 125, new Color("378030"), EnumMetadataBehavior.SPECIFIC)); //dark_oak

		//ghast tear
		add(new CompostRegistryEntry(new ItemStack(Items.GHAST_TEAR, 1), 5, new Color("FFFFFF"), EnumMetadataBehavior.IGNORED));
		//rotten flesh
		add(new CompostRegistryEntry(new ItemStack(Items.ROTTEN_FLESH, 1), 85, new Color("C45631"), EnumMetadataBehavior.IGNORED));
		//spider eye
		add(new CompostRegistryEntry(new ItemStack(Items.SPIDER_EYE, 1), 85, new Color("963E44"), EnumMetadataBehavior.IGNORED));
		//spider eye fermented
		add(new CompostRegistryEntry(new ItemStack(Items.FERMENTED_SPIDER_EYE, 1), 85, new Color("963E44"), EnumMetadataBehavior.IGNORED));
		
		//dandelion
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), 1), 100, new Color("FFF461"), EnumMetadataBehavior.SPECIFIC));
		//poppy
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 0), 100, new Color("FF1212"), EnumMetadataBehavior.SPECIFIC));
		//blue orchid
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 1), 100, new Color("33CFFF"), EnumMetadataBehavior.SPECIFIC));
		//allium
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 2), 100, new Color("F59DFA"), EnumMetadataBehavior.SPECIFIC));
		//azure bluet
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 3), 100, new Color("E3E3E3"), EnumMetadataBehavior.SPECIFIC));
		//red_tulip
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 4), 100, new Color("FF3D12"), EnumMetadataBehavior.SPECIFIC));
		//orange tulip
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 5), 100, new Color("FF7E29"), EnumMetadataBehavior.SPECIFIC));
		//white tulip
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 6), 100, new Color("FFFFFF"), EnumMetadataBehavior.SPECIFIC));
		//pink tulip
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 7), 100, new Color("F5C4FF"), EnumMetadataBehavior.SPECIFIC));
		//oxeye daisy
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, 8), 100, new Color("E9E9E9"), EnumMetadataBehavior.SPECIFIC));

		//sunflower
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 0), 100, new Color("FFDD00"), EnumMetadataBehavior.SPECIFIC));
		//lilac
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 1), 100, new Color("FCC7F0"), EnumMetadataBehavior.SPECIFIC));
		//tall GRASS
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 2), 100, new Color("23630E"), EnumMetadataBehavior.SPECIFIC));
		//large fern
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 3), 100, new Color("23630E"), EnumMetadataBehavior.SPECIFIC));
		//rose bush
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 4), 100, new Color("FF1212"), EnumMetadataBehavior.SPECIFIC));
		//peony
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, 5), 100, new Color("F3D2FC"), EnumMetadataBehavior.SPECIFIC));

		//mushroom_brown
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), 1), 100, new Color("CFBFB6"), EnumMetadataBehavior.IGNORED));
		//mushroom_red
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.RED_MUSHROOM), 1), 100, new Color("D6A8A5"), EnumMetadataBehavior.IGNORED));
		
		//wheat
		add(new CompostRegistryEntry(new ItemStack(Items.WHEAT, 1), 85, new Color("E3E162"), EnumMetadataBehavior.IGNORED));
		//bread
		add(new CompostRegistryEntry(new ItemStack(Items.BREAD, 1), 125, new Color("D1AF60"), EnumMetadataBehavior.IGNORED));

		//pumpkin pie
		add(new CompostRegistryEntry(new ItemStack(Items.PUMPKIN_PIE, 1), 175, new Color("E39A6D"), EnumMetadataBehavior.IGNORED));
		//egg
		add(new CompostRegistryEntry(new ItemStack(Items.EGG, 1), 80, new Color("FFFA66"), EnumMetadataBehavior.IGNORED));

		//pork
		add(new CompostRegistryEntry(new ItemStack(Items.PORKCHOP, 1), 175, new Color("FFA091"), EnumMetadataBehavior.IGNORED));
		//cooked pork
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_PORKCHOP, 1), 175, new Color("FFFDBD"), EnumMetadataBehavior.IGNORED));
	
		//beef
		add(new CompostRegistryEntry(new ItemStack(Items.BEEF, 1), 175, new Color("FF4242"), EnumMetadataBehavior.IGNORED));
		//cooked beef
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_BEEF, 1), 175, new Color("80543D"), EnumMetadataBehavior.IGNORED));

		//mutton
		add(new CompostRegistryEntry(new ItemStack(Items.MUTTON, 1), 175, new Color("FF4242"), EnumMetadataBehavior.IGNORED));
		//cooked mutton
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_MUTTON, 1), 175, new Color("80543D"), EnumMetadataBehavior.IGNORED));

		//chicken
		add(new CompostRegistryEntry(new ItemStack(Items.CHICKEN, 1), 175, new Color("FFE8E8"), EnumMetadataBehavior.IGNORED));
		//cooked chicken
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_CHICKEN, 1), 175, new Color("FA955F"), EnumMetadataBehavior.IGNORED));

		//rabbit
		add(new CompostRegistryEntry(new ItemStack(Items.RABBIT, 1), 175, new Color("FFE8E8"), EnumMetadataBehavior.IGNORED));
		//cooked rabbit
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_RABBIT, 1), 175, new Color("FA955F"), EnumMetadataBehavior.IGNORED));

		//fish
		add(new CompostRegistryEntry(new ItemStack(Items.FISH, 1, 0), 120, new Color("6DCFB3"), EnumMetadataBehavior.SPECIFIC));
		//cooked fish
		add(new CompostRegistryEntry(new ItemStack(Items.COOKED_FISH, 1, 0), 120, new Color("D8EBE5"), EnumMetadataBehavior.SPECIFIC));

		//salmon
		add(new CompostRegistryEntry(new ItemStack(Items.FISH, 1, 1), 120, new Color("FF2E4A"), EnumMetadataBehavior.SPECIFIC));
		//cooked salmon
		add(new CompostRegistryEntry(new ItemStack(Items.FISH, 1, 1), 120, new Color("E87A3F"), EnumMetadataBehavior.SPECIFIC));

		//clownfish
		add(new CompostRegistryEntry(new ItemStack(Items.FISH, 1, 2), 120, new Color("FF771C"), EnumMetadataBehavior.SPECIFIC));
		//blowfish
		add(new CompostRegistryEntry(new ItemStack(Items.FISH, 1, 3), 120, new Color("DBFAFF"), EnumMetadataBehavior.SPECIFIC));

		//apple
		add(new CompostRegistryEntry(new ItemStack(Items.APPLE, 1), 100, new Color("FFF68F"), EnumMetadataBehavior.IGNORED));
		//golden apple
		add(new CompostRegistryEntry(new ItemStack(Items.GOLDEN_APPLE, 1), 200, new Color("CCCC00"), EnumMetadataBehavior.IGNORED));
		//melon slice
		add(new CompostRegistryEntry(new ItemStack(Items.MELON, 1), 40, new Color("FF443B"), EnumMetadataBehavior.IGNORED));
		//melon
		add(new CompostRegistryEntry(new ItemStack(Blocks.MELON_BLOCK, 1), 150, new Color("FF443B"), EnumMetadataBehavior.IGNORED));
		//pumpkin
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN), 1), 150, new Color("FFDB66"), EnumMetadataBehavior.IGNORED));
		//jack o lantern
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.LIT_PUMPKIN), 1), 150, new Color("FFDB66"), EnumMetadataBehavior.IGNORED));
		//cactus
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.CACTUS), 1), 100, new Color("DEFFB5"), EnumMetadataBehavior.IGNORED));

		//carrot
		add(new CompostRegistryEntry(new ItemStack(Items.CARROT, 1), 80, new Color("FF9B0F"), EnumMetadataBehavior.IGNORED));
		//potato
		add(new CompostRegistryEntry(new ItemStack(Items.POTATO, 1), 80, new Color("FFF1B5"), EnumMetadataBehavior.IGNORED));
		//baked potato
		add(new CompostRegistryEntry(new ItemStack(Items.BAKED_POTATO, 1), 80, new Color("FFF1B5"), EnumMetadataBehavior.IGNORED));
		//poison potato
		add(new CompostRegistryEntry(new ItemStack(Items.POISONOUS_POTATO, 1), 80, new Color("E0FF8A"), EnumMetadataBehavior.IGNORED));

		//waterlily
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY), 1), 80, new Color("269900"), EnumMetadataBehavior.IGNORED));
		//vine
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.VINE), 1), 80, new Color("23630E"), EnumMetadataBehavior.IGNORED));
		//tall GRASS
		add(new CompostRegistryEntry(new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1), 80, new Color("23630E"), EnumMetadataBehavior.IGNORED));
		//netherwart
		add(new CompostRegistryEntry(new ItemStack(Items.NETHER_WART, 1), 80, new Color("FF2B52"), EnumMetadataBehavior.IGNORED));
		//sugar cane
		add(new CompostRegistryEntry(new ItemStack(Items.REEDS, 1), 80, new Color("9BFF8A"), EnumMetadataBehavior.IGNORED));
		//string
		add(new CompostRegistryEntry(new ItemStack(Items.STRING, 1), 40, new Color("FFFFFF"), EnumMetadataBehavior.IGNORED));
		//ASH
		add(new CompostRegistryEntry(new ItemStack(ENOItems.ASH, 1), 40, new Color("C2D6FF"), EnumMetadataBehavior.IGNORED));
	}
}
