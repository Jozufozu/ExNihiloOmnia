package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ore.files.OreLoader;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OreRegistry {
    public static boolean oredict_ingots = true;

    public static boolean force_ores = false;

    private static final String path = ENO.path + File.separator + "registries" + File.separator + "ore" + File.separator;

    public static HashMap<String, String> metadatas = new HashMap<String, String>();
    public static HashMap<String, Block> blocks = new HashMap<String, Block>();
    public static HashMap<String, Ore> registry = new HashMap<String, Ore>();

    public static void init() {

        for (EnumOreBlockType type : EnumOreBlockType.values())
            new BlockRemap(type);

        loadNameMetaLookup();

        setupDefaults();

        if (!force_ores)
            filterOreDict();

        List<Ore> loaded = OreLoader.load(path);

        if (loaded != null && !loaded.isEmpty()) {
            for (Ore entry : loaded) {
                register(entry);
            }
        }

        List<String> banList = getOreList(path + File.separator + "blacklist.txt");

        for (String ban : banList) {
            registry.remove(ban);
        }

        saveNameMetaLookup();
        getBlocks();
    }

    public static void loadNameMetaLookup() {
        File[] files = new File(path).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().equals("metadatas.json")) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));

                        if (reader.ready()) {
                            metadatas = OreLoader.gson.fromJson(reader, HashMap.class);
                        }

                        reader.close();
                    } catch (Exception e) {
                        ENO.log.error("Failed to read sieve recipe file: '" + file + "'.");
                        ENO.log.error(e);
                    }
                }
            }
        }
    }

    public static void saveNameMetaLookup() {
        File file = new File(path + "metadatas.json");

        ENO.log.info("Attempting to dump ore list: '" + file + "'.");

        FileWriter writer;

        try {
            file.getParentFile().mkdirs();

            writer = new FileWriter(file);
            writer.write(OreLoader.gson.toJson(metadatas));
            writer.close();
        } catch (Exception e) {
            ENO.log.error("Failed to write file: '" + file + "'.");
            ENO.log.error(e);
        }
    }

    public static List<String> getOreList(String file) {
        List<String> list = new ArrayList<String>();

        try {
            File f = new File(file);

            if (f.createNewFile()) {
                FileWriter writer = new FileWriter(f);
                writer.write("#each ore should be lower case and be on a new line");
                writer.close();
            }

            FileInputStream stream = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                if (!strLine.startsWith("#"))
                    list.add(strLine);
            }

            br.close();
        } catch (Exception e) {
            ENO.log.error(e);
        }

        return list;
    }

    public static void filterOreDict() {
        List<String> toBeRemoved = new ArrayList<String>();

        for (Ore ore : registry.values()) {
            List<String> names = new ArrayList<String>();

            names.add(ore.getOreDictName("ingot"));

            List<String> internalNames = ore.getOreDictNames();

            if (internalNames.size() > 0) {
                for (String s : internalNames)
                    names.add("ingot" + s);
            }

            boolean present = false;

            if (ore.getParentMod() != null)
                present = Loader.isModLoaded(ore.getParentMod());

            for (String name : names) {
                if (OreDictionary.doesOreNameExist(name)) {
                    present = true;
                    break;
                }
            }

            if (!present) toBeRemoved.add(ore.getName());
        }

        List<String> whiteList = getOreList(path + File.separator + "whitelist.txt");

        for (String ore : toBeRemoved)
            if (!whiteList.contains(ore)) registry.remove(ore);
    }

    public static void getBlocks() {
        for (Ore ore : registry.values())
            blocks.put(ore.getName(), new BlockOre(ore));
    }

    public static void setupSmelting() {
        for (Ore ore : registry.values())
            ore.addSmelting();
    }

    public static void setupCrafting() {
        for (Ore ore : registry.values())
            ore.addCrafting();
    }

    public static void setupOreDict() {
        if (oredict_ingots) {
            for (Ore ore : registry.values()) {
                if (ore.getIngot() == null) {
                    OreDictionary.registerOre(ore.getOreDictName("ingot"), new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()));

                    if (ore.getOreDictNames().size() > 0) {
                        for (String name : ore.getOreDictNames())
                            OreDictionary.registerOre(name, new ItemStack(ENOItems.INGOT_ORE, 1, ore.getMetadata()));
                    }
                }
            }
        }
    }

    public static void register(Ore ore) {
        String name = ore.getName();

        if (registry.containsKey(name))
            registry.remove(name);

        if (metadatas.containsKey(name))
            ore.setMeta(Integer.valueOf(metadatas.get(name)));
        else
            metadatas.put(ore.getName(), String.valueOf(ore.getMetadata()));

        registry.put(name, ore);
    }

    @Nullable
    public static Block getBlockFromOre(Ore ore) {
        if (registry.containsKey(ore.getName()))
            return blocks.get(ore.getName());
        else {
            ENO.log.error("Ore " + ore.getName() + " is not registered!");
            return null;
        }
    }

    public static Ore getOre(Item item) {
        return getOre(Block.getBlockFromItem(item));
    }

    @Nullable
    public static Ore getOre(Block block) {
        String oreName = "";
        for (Map.Entry<String, Block> entry : blocks.entrySet()) {
            if (entry.getValue() == block) {
                oreName = entry.getKey();
                break;
            }
        }

        return registry.get(oreName);
    }

    public static Ore getOre(String name) {
        return registry.get(name);
    }

    public static Ore getOre(int meta) {
        for (Ore ore : registry.values())
            if (ore.getMetadata() == meta) return ore;

        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void regColors() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                Ore ore = getOre(stack.getMetadata());
                return tintIndex == 1 && ore != null ? ore.getColor() : -1;
            }
        }, ENOItems.BROKEN_ORE, ENOItems.BROKEN_ORE_ENDER, ENOItems.BROKEN_ORE_NETHER, ENOItems.CRUSHED_ORE, ENOItems.POWDERED_ORE, ENOItems.INGOT_ORE);

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                Ore ore = getOre(state.getBlock());
                return tintIndex == 0 && ore != null ? ore.getColor() : -1;
            }
        }, blocks.values().toArray(new Block[]{}));

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                Ore ore = getOre(Block.getBlockFromItem(stack.getItem()));
                return tintIndex == 0 && ore != null ? ore.getColor() : -1;
            }
        }, blocks.values().toArray(new Block[]{}));
    }

    public static void setupDefaults() {
        register(new Ore("iron", new Color("F2AB7C"), 30, true, true, false).setIngot(Items.IRON_INGOT));
        register(new Ore("gold", new Color("FFD000"), 10, true, true, false).setIngot(Items.GOLD_INGOT));

        register(new Ore("tin", new Color("ABC9B6"), 15, true, false, true));
        register(new Ore("copper", new Color("F46E00"), 20, true, true, false));
        register(new Ore("lead", new Color("2D2563"), 10, true, false, true));
        register(new Ore("silver", new Color("8CC9FF"), 5, true, false, true));
        register(new Ore("nickel", new Color("BAB877"), 10, true, true, false).addOreDictName("ingotFerrous"));
        register(new Ore("platinum", new Color("38CDFF"), 2, true, false, true).addOreDictName("ingotShiny"));
        register(new Ore("aluminum", new Color("FFC7C7"), 20, true, false, true).addOreDictName("ingotAluminium"));

        register(new Ore("osmium", new Color("608FC4"), 20, true, false, false));

        register(new Ore("ardite", new Color("FF4D00"), 2, false, true, false).setParentMod("tconstruct"));
        register(new Ore("cobalt", new Color("0B91FF"), 2, false, true, false).setParentMod("tconstruct"));

        register(new Ore("draconium", new Color("733DAB"), 4, false, false, true).setParentMod("draconicevolution"));
        register(new Ore("yellorite", new Color("B6E324"), 2, true, false, false).setIngot(Item.REGISTRY.getObject(new ResourceLocation("bigreactors", "ingotMetals"))).addOreDictName("ingotYellorium").addOreDictName("ingotUranium").setParentMod("bigreactors"));
        register(new Ore("uranium", new Color("47503F"), 2, true, false, false));
    }
}
