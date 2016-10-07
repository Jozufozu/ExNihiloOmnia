package exnihiloomnia.compatibility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.enums.EnumOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ENOOres {
    private static String ORE_CATAGORY = "ores";

    public static boolean force_ores;

    public static boolean force_copper;
    public static boolean force_tin;
    public static boolean force_lead;
    public static boolean force_silver;
    public static boolean force_platinum;
    public static boolean force_nickel;
    public static boolean force_osmium;
    public static boolean force_ardite;
    public static boolean force_cobalt;
    public static boolean force_aluminum;

    public static boolean oredict_gravels;
    public static boolean oredict_sand;
    public static boolean oredict_dust;
    public static boolean oredict_ingots;

    public static List<EnumOre> getActiveOres() {
        force_ores = ENO.config.get(ORE_CATAGORY, "force all ores to be added", false).getBoolean(false);

        force_copper = ENO.config.get(ORE_CATAGORY, "force copper", false).getBoolean(false);
        force_tin = ENO.config.get(ORE_CATAGORY, "force tin", false).getBoolean(false);
        force_aluminum = ENO.config.get(ORE_CATAGORY, "force aluminum", false).getBoolean(false);
        force_lead = ENO.config.get(ORE_CATAGORY, "force lead", false).getBoolean(false);
        force_silver = ENO.config.get(ORE_CATAGORY, "force silver", false).getBoolean(false);
        force_platinum = ENO.config.get(ORE_CATAGORY, "force platinum", false).getBoolean(false);
        force_nickel = ENO.config.get(ORE_CATAGORY, "force nickel", false).getBoolean(false);
        force_osmium = ENO.config.get(ORE_CATAGORY, "force osmium", false).getBoolean(false);
        force_ardite = ENO.config.get(ORE_CATAGORY, "force ardite", false).getBoolean(false);
        force_cobalt = ENO.config.get(ORE_CATAGORY, "force cobalt", false).getBoolean(false);

        oredict_gravels = ENO.config.get(ORE_CATAGORY, "add gravel ores to the ore dictionary", false).getBoolean(false);
        oredict_sand = ENO.config.get(ORE_CATAGORY, "add sand ores to the ore dictionary", false).getBoolean(false);
        oredict_dust = ENO.config.get(ORE_CATAGORY, "add dust ores to the ore dictionary", false).getBoolean(false);
        oredict_ingots = ENO.config.get(ORE_CATAGORY, "add ingots to the ore dictionary", true).getBoolean(true);

        List<EnumOre> ores = new ArrayList<EnumOre>();
        
        //change to true to debug
        if (force_ores) {
            Collections.addAll(ores, EnumOre.values());
            return ores;
        }

        //always add iron and gold
        ores.add(EnumOre.GOLD);
        ores.add(EnumOre.IRON);

        if (OreDictionary.getOres("oreCopper").size() > 0 || force_copper)
            ores.add(EnumOre.COPPER);
        
        if (OreDictionary.getOres("oreTin").size() > 0 || force_tin)
            ores.add(EnumOre.TIN);
        
        if (OreDictionary.getOres("oreLead").size() > 0 || force_lead)
            ores.add(EnumOre.LEAD);
        
        if (OreDictionary.getOres("oreSilver").size() > 0 || force_silver)
            ores.add(EnumOre.SILVER);
        
        if (OreDictionary.getOres("oreArdite").size() > 0 || force_ardite)
            ores.add(EnumOre.ARDITE);
        
        if (OreDictionary.getOres("oreCobalt").size() > 0 || force_cobalt)
            ores.add(EnumOre.COBALT);
        
        if (OreDictionary.getOres("oreNickel").size() > 0 || OreDictionary.getOres("oreFerrous").size() > 0 || force_nickel)
            ores.add(EnumOre.NICKEL);
        
        if (OreDictionary.getOres("orePlatinum").size() > 0 || OreDictionary.getOres("oreShiny").size() > 0 || force_platinum)
            ores.add(EnumOre.PLATINUM);
        
        if (OreDictionary.getOres("oreAluminum").size() > 0 || OreDictionary.getOres("oreAluminium").size() > 0 || force_aluminum)
            ores.add(EnumOre.ALUMINUM);

        if (OreDictionary.getOres("oreOsmium").size() > 0 || force_osmium)
            ores.add(EnumOre.OSMIUM);

        return ores;
    }

    public static void addOreDict() {
        for (EnumOre ore : ENO.oreList) {
            if (!(ore == EnumOre.IRON || ore == EnumOre.GOLD) && oredict_ingots)
                OreDictionary.registerOre(ore.getOreDictName("ingot"), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()));
            
            if (ore.hasGravel() && oredict_gravels)
                OreDictionary.registerOre(ore.getOreDictName("ore"), new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()));
            
            if (ore.hasEnd() && oredict_gravels)
                OreDictionary.registerOre(ore.getOreDictName("ore"), new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()));
            
            if (ore.hasNether() && oredict_gravels)
                OreDictionary.registerOre(ore.getOreDictName("ore"), new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()));
            
            if (oredict_sand)
                OreDictionary.registerOre(ore.getOreDictName("ore"), new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()));
            
            if (oredict_dust)
                OreDictionary.registerOre(ore.getOreDictName("ore"), new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()));
        }
    }

    public static void addCrafting() {
        for (EnumOre ore : ENO.oreList) {
            if (ore.hasGravel())
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE, 1, ore.getMetadata())));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata())));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata())));
            if (ore.hasNether())
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE_NETHER, 1, ore.getMetadata())));
            if (ore.hasEnd())
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE_ENDER, 1, ore.getMetadata())));
        }
    }

    public static void addSmelting() {
        for (EnumOre ore : ENO.oreList) {
            if (ore.equals(EnumOre.IRON)) {
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()), new ItemStack(Items.IRON_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()), new ItemStack(Items.IRON_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()), new ItemStack(Items.IRON_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()), new ItemStack(Items.IRON_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()), new ItemStack(Items.IRON_INGOT), 0);
            }
            else if (ore.equals(EnumOre.GOLD)) {
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()), new ItemStack(Items.GOLD_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()), new ItemStack(Items.GOLD_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()), new ItemStack(Items.GOLD_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()), new ItemStack(Items.GOLD_INGOT), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()), new ItemStack(Items.GOLD_INGOT), 0);
            }
            else {
                if (ore.hasGravel())
                    GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()), 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()), 0);
                if (ore.hasNether())
                    GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()), 0);
                if (ore.hasEnd())
                    GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()), 0);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void regColors() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return tintIndex == 1 ? EnumOre.fromMetadata(stack.getMetadata()).getColor() : -1;
            }
        }, new Item[]{ENOItems.BROKEN_ORE, ENOItems.BROKEN_ORE_ENDER, ENOItems.BROKEN_ORE_NETHER, ENOItems.CRUSHED_ORE, ENOItems.POWDERED_ORE, ENOItems.INGOT_ORE});
    }
}
