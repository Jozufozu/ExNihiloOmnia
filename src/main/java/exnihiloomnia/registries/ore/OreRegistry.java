package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class OreRegistry {

    public static HashMap<String, Block> blocks = new HashMap<String, Block>();

    public static HashMap<String, Ore> registry = new HashMap<String, Ore>();

    public static void init() {
        setupDefaults();
    }

    public static void register(Ore ore) {
        registry.put(ore.getName(), ore);
        blocks.put(ore.getName(), new BlockOre(ore));

        ore.addCrafting();
        ore.addSmelting();
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
        register(new Ore("iron", new Color("F2AB7C"), 30, true, true, false));
        register(new Ore("gold", new Color("FFD000"), 10, true, true, false));
        register(new Ore("tin", new Color("ABC9B6"), 15, true, false, true));
        register(new Ore("copper", new Color("F46E00"), 20, true, true, false));
        register(new Ore("lead", new Color("2D2563"), 10, true, false, true));
        register(new Ore("silver", new Color("8CC9FF"), 5, true, false, true));
        register(new Ore("nickel", new Color("BAB877"), 10, true, true, false));
        register(new Ore("platinum", new Color("38CDFF"), 2, true, false, true));
        register(new Ore("aluminum", new Color("FFC7C7"), 20, true, false, true));
        register(new Ore("osmium", new Color("608FC4"), 20, true, false, false));
        register(new Ore("ardite", new Color("FF4D00"), 2, false, true, false));
        register(new Ore("cobalt", new Color("0B91FF"), 2, false, true, false));
        register(new Ore("draconium", new Color("733DAB"), 4, false, false, true));
        register(new Ore("yellorite", new Color("B6E324"), 2, true, false, false));
        register(new Ore("uranium", new Color("47503F"), 2, true, false, false));
    }
}
