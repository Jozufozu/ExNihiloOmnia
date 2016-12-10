package exnihiloomnia.items;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.buckets.ItemBucketPorcelain;
import exnihiloomnia.items.buckets.ItemBucketPorcelainMilk;
import exnihiloomnia.items.buckets.UniversalPorcelainBucket;
import exnihiloomnia.items.crooks.ItemCrook;
import exnihiloomnia.items.hammers.ItemHammer;
import exnihiloomnia.items.materials.ENOToolMaterials;
import exnihiloomnia.items.meshs.ItemMesh;
import exnihiloomnia.items.misc.*;
import exnihiloomnia.items.ores.ItemOre;
import exnihiloomnia.items.sieveassist.ItemSifter;
import exnihiloomnia.util.enums.EnumOreItemType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ENOItems {
	
    public static final CreativeTabs ENO_TAB = new CreativeTabs("tabElse") {
        @Override public Item getTabIconItem() {
            return Item.getItemFromBlock(ENOBlocks.SIEVE_WOOD);
        }
    };
    
    public static final CreativeTabs ORE_TAB = new CreativeTabs("tabOre") {
        @Override public Item getTabIconItem() {
            return BROKEN_ORE;
        }
    };

	public static Item ASTROLABE_JADE;

    public static UniversalPorcelainBucket BUCKET_PORCELAIN;

	public static Item BUCKET_PORCELAIN_RAW;
	public static Item BUCKET_PORCELAIN_EMPTY;
	public static Item BUCKET_PORCELAIN_WATER;
	public static Item BUCKET_PORCELAIN_LAVA;
	public static Item BUCKET_PORCELAIN_MILK;
	public static Item BUCKET_PORCELAIN_WITCHWATER;

	public static Item CROOK_WOOD;
	public static Item CROOK_BONE;
	
	public static Item HAMMER_WOOD;
	public static Item HAMMER_STONE;
	public static Item HAMMER_IRON;
	public static Item HAMMER_GOLD;
	public static Item HAMMER_DIAMOND;

    public static Item SIFTER_IRON;
    public static Item SIFTER_GOLD;
    public static Item SIFTER_DIAMOND;

	public static Item MESH_SILK_WHITE;
	public static Item MESH_WOOD;
	
	public static Item ASH;
	public static Item PORCELAIN;
	public static Item STONE;

	public static Item SILKWORM;
	public static Item COOKED_SILKWORM;

    public static Item SPORES;
    public static Item SUGARCANE_SEEDS;
    public static Item CACTUS_SEEDS;
    public static Item POTATO_SEEDS;
    public static Item CARROT_SEEDS;
    public static Item GRASS_SEEDS;
    public static Item CHORUS_SEEDS;

    public static Item SEED_ACACIA;
    public static Item SEED_BIRCH;
    public static Item SEED_JUNGLE;
    public static Item SEED_OAK;
    public static Item SEED_SPRUCE;
    public static Item SEED_DARK_OAK;

    public static Item PORCELAIN_DOLL;
    public static Item END_DOLL;
    public static Item BLAZE_DOLL;

    public static Item BROKEN_ORE;
    public static Item BROKEN_ORE_NETHER;
    public static Item BROKEN_ORE_ENDER;
    public static Item CRUSHED_ORE;
    public static Item POWDERED_ORE;
    public static Item INGOT_ORE;

    public static List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();

        items.add(BUCKET_PORCELAIN_RAW);
        items.add(BUCKET_PORCELAIN_EMPTY);
        items.add(BUCKET_PORCELAIN_LAVA);
        items.add(BUCKET_PORCELAIN_WATER);
        items.add(BUCKET_PORCELAIN_WITCHWATER);
        items.add(BUCKET_PORCELAIN_MILK);

        items.add(CROOK_BONE);
        items.add(CROOK_WOOD);

        items.add(HAMMER_DIAMOND);
        items.add(HAMMER_GOLD);
        items.add(HAMMER_IRON);
        items.add(HAMMER_STONE);
        items.add(HAMMER_WOOD);

        items.add(SIFTER_DIAMOND);
        items.add(SIFTER_GOLD);
        items.add(SIFTER_IRON);

        items.add(MESH_SILK_WHITE);
        
        if (!ENOConfig.classic_sieve)
            items.add(MESH_WOOD);
        
        items.add(PORCELAIN);
        items.add(SPORES);
        items.add(STONE);
        items.add(SILKWORM);
        items.add(COOKED_SILKWORM);
        items.add(ASH);
        items.add(POTATO_SEEDS);
        items.add(CARROT_SEEDS);
        items.add(SUGARCANE_SEEDS);
        items.add(CACTUS_SEEDS);
        items.add(GRASS_SEEDS);
        items.add(CHORUS_SEEDS);
        items.add(SEED_ACACIA);
        items.add(SEED_BIRCH);
        items.add(SEED_JUNGLE);
        items.add(SEED_OAK);
        items.add(SEED_SPRUCE);
        items.add(SEED_DARK_OAK);
        items.add(PORCELAIN_DOLL);
        items.add(END_DOLL);
        items.add(BLAZE_DOLL);
        items.add(BROKEN_ORE);
        items.add(BROKEN_ORE_NETHER);
        items.add(BROKEN_ORE_ENDER);
        items.add(CRUSHED_ORE);
        items.add(POWDERED_ORE);
        items.add(INGOT_ORE);
        items.add(ASTROLABE_JADE);

        return items;
    }

	public static void init() {
        POTATO_SEEDS = new ItemSeeds(Blocks.POTATOES, Blocks.FARMLAND).setUnlocalizedName("seeds_potato").setRegistryName("seeds_potato").setCreativeTab(ENO_TAB);
        CARROT_SEEDS = new ItemSeeds(Blocks.CARROTS, Blocks.FARMLAND).setUnlocalizedName("seeds_carrot").setRegistryName("seeds_carrot").setCreativeTab(ENO_TAB);
        SUGARCANE_SEEDS = new ItemBlockSpecial(Blocks.REEDS).setUnlocalizedName("seeds_sugarcane").setRegistryName("seeds_sugarcane").setCreativeTab(ENO_TAB);
        CACTUS_SEEDS = new ItemBlockSpecial(Blocks.CACTUS).setUnlocalizedName("seeds_cactus").setRegistryName("seeds_cactus").setCreativeTab(ENO_TAB);
        GRASS_SEEDS = new ItemGrassSeeds().setUnlocalizedName("seeds_grass").setRegistryName("seeds_grass");
        CHORUS_SEEDS = new ItemBlockSpecial(ENOBlocks.CHORUS_SPROUT).setUnlocalizedName("seeds_chorus").setRegistryName("seeds_chorus").setCreativeTab(ENO_TAB);
        SPORES = new ItemSpores().setUnlocalizedName("spores").setRegistryName("spores");

        ASTROLABE_JADE = new ItemAstrolabe().setUnlocalizedName("astrolabe_jade").setRegistryName("astrolabe_jade");

        BUCKET_PORCELAIN_RAW = new Item().setUnlocalizedName("bucket_porcelain_raw").setRegistryName("bucket_porcelain_raw").setCreativeTab(ENO_TAB);
        BUCKET_PORCELAIN_EMPTY = new ItemBucketPorcelain(Blocks.AIR).setUnlocalizedName("bucket_porcelain_empty").setRegistryName("bucket_porcelain_empty").setMaxStackSize(16);
        BUCKET_PORCELAIN_WATER = new ItemBucketPorcelain(Blocks.FLOWING_WATER).setUnlocalizedName("bucket_porcelain_water").setRegistryName("bucket_porcelain_water");
        BUCKET_PORCELAIN_LAVA = new ItemBucketPorcelain(Blocks.FLOWING_LAVA).setUnlocalizedName("bucket_porcelain_lava").setRegistryName("bucket_porcelain_lava");
        BUCKET_PORCELAIN_MILK = new ItemBucketPorcelainMilk().setUnlocalizedName("bucket_porcelain_milk").setRegistryName("bucket_porcelain_milk");
        BUCKET_PORCELAIN_WITCHWATER = new ItemBucketPorcelain(ENOBlocks.WITCHWATER).setUnlocalizedName("bucket_porcelain_witchwater").setRegistryName("bucket_porcelain_witchwater");

        CROOK_WOOD = new ItemCrook(ENOToolMaterials.STICK).setUnlocalizedName("crook_wood").setRegistryName("crook_wood");
        CROOK_BONE = new ItemCrook(ENOToolMaterials.BONE).setUnlocalizedName("crook_bone").setRegistryName("crook_bone");

        HAMMER_WOOD = new ItemHammer(ToolMaterial.WOOD).setUnlocalizedName("hammer_wood").setRegistryName("hammer_wood");
        HAMMER_STONE = new ItemHammer(ToolMaterial.STONE).setUnlocalizedName("hammer_stone").setRegistryName("hammer_stone");
        HAMMER_IRON = new ItemHammer(ToolMaterial.IRON).setUnlocalizedName("hammer_iron").setRegistryName("hammer_iron");
        HAMMER_GOLD = new ItemHammer(ToolMaterial.GOLD).setUnlocalizedName("hammer_gold").setRegistryName("hammer_gold");
        HAMMER_DIAMOND = new ItemHammer(ToolMaterial.DIAMOND).setUnlocalizedName("hammer_diamond").setRegistryName("hammer_diamond");

        SIFTER_DIAMOND = new ItemSifter(ToolMaterial.DIAMOND).setMaxDamage(127).setUnlocalizedName("sifter_diamond").setRegistryName("sifter_diamond");
        SIFTER_GOLD = new ItemSifter(ToolMaterial.GOLD).setMaxDamage(47).setUnlocalizedName("sifter_gold").setRegistryName("sifter_gold");
        SIFTER_IRON = new ItemSifter(ToolMaterial.IRON).setMaxDamage(95).setUnlocalizedName("sifter_iron").setRegistryName("sifter_iron");

        MESH_SILK_WHITE = new ItemMesh().setUnlocalizedName("mesh_silk_white").setRegistryName("mesh_silk_white").setMaxDamage(63);
        MESH_WOOD = new ItemMesh().setUnlocalizedName("mesh_wood").setRegistryName("mesh_wood").setMaxDamage(9);

        ASH = new ItemAsh().setUnlocalizedName("ash").setRegistryName("ash");
        PORCELAIN = new Item().setUnlocalizedName("porcelain").setRegistryName("porcelain").setCreativeTab(ENOItems.ENO_TAB);
        STONE = new ItemStone().setUnlocalizedName("stone").setRegistryName("stone");
        SILKWORM = new ItemSilkworm().setUnlocalizedName("silkworm").setRegistryName("silkworm");
        COOKED_SILKWORM = new ItemFood(3, 0.2f, false).setUnlocalizedName("cooked_silkworm").setRegistryName("cooked_silkworm").setCreativeTab(ENO_TAB);
        PORCELAIN_DOLL = new Item().setUnlocalizedName("porcelain_doll").setRegistryName("doll").setCreativeTab(ENO_TAB);
        BLAZE_DOLL = new Item().setUnlocalizedName("doll_blaze").setRegistryName("doll_blaze").setCreativeTab(ENO_TAB);
        END_DOLL = new Item().setUnlocalizedName("doll_ender").setRegistryName("doll_end").setCreativeTab(ENO_TAB);

        SEED_OAK = new ItemBlockMeta(Blocks.SAPLING, 0).setUnlocalizedName("seed_oak").setRegistryName("seed_oak").setCreativeTab(ENO_TAB);
        SEED_SPRUCE = new ItemBlockMeta(Blocks.SAPLING, 1).setUnlocalizedName("seed_spruce").setRegistryName("seed_spruce").setCreativeTab(ENO_TAB);
        SEED_BIRCH = new ItemBlockMeta(Blocks.SAPLING, 2).setUnlocalizedName("seed_birch").setRegistryName("seed_birch").setCreativeTab(ENO_TAB);
        SEED_JUNGLE = new ItemBlockMeta(Blocks.SAPLING, 3).setUnlocalizedName("seed_jungle").setRegistryName("seed_jungle").setCreativeTab(ENO_TAB);
        SEED_ACACIA = new ItemBlockMeta(Blocks.SAPLING, 4).setUnlocalizedName("seed_acacia").setRegistryName("seed_acacia").setCreativeTab(ENO_TAB);
        SEED_DARK_OAK = new ItemBlockMeta(Blocks.SAPLING, 5).setUnlocalizedName("seed_dark_oak").setRegistryName("seed_dark_oak").setCreativeTab(ENO_TAB);

        BROKEN_ORE = new ItemOre(EnumOreItemType.BROKEN).setUnlocalizedName("ore_broken").setRegistryName("ore_broken");
        BROKEN_ORE_NETHER = new ItemOre(EnumOreItemType.BROKEN_NETHER).setUnlocalizedName("ore_broken_nether").setRegistryName("ore_broken_nether");
        BROKEN_ORE_ENDER = new ItemOre(EnumOreItemType.BROKEN_ENDER).setUnlocalizedName("ore_broken_ender").setRegistryName("ore_broken_ender");
        CRUSHED_ORE = new ItemOre(EnumOreItemType.CRUSHED).setUnlocalizedName("ore_crushed").setRegistryName("ore_crushed");
        POWDERED_ORE = new ItemOre(EnumOreItemType.POWDERED).setUnlocalizedName("ore_powder").setRegistryName("ore_powder");
        INGOT_ORE = new ItemOre(EnumOreItemType.INGOT).setUnlocalizedName("ore_ingot").setRegistryName("ore_ingot");

        for (Item item : getItems())
		    GameRegistry.register(item);

        if (ENOConfig.universal_bucket) {
            BUCKET_PORCELAIN = new UniversalPorcelainBucket();
            GameRegistry.register(BUCKET_PORCELAIN);
        }
	}
}
