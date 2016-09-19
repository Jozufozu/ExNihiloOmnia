package exnihiloomnia.registries.crucible;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.crucible.files.CrucibleRecipeLoader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CrucibleRegistry {
    public static HashMap<String, CrucibleRegistryEntry> entries;

    public static HashMap<String, CrucibleRegistryEntry> getEntryMap() {
        return entries;
    }

    public static void initialize() {
        entries = new HashMap<String, CrucibleRegistryEntry>();

        if (ENORegistries.loadCrucibleDefaults)
            registerMeltables();

        List<CrucibleRegistryEntry> loaded = CrucibleRecipeLoader.load(ENO.path + File.separator + "registries" + File.separator + "crucible" + File.separator);

        if (loaded != null && !loaded.isEmpty()) {
            for (CrucibleRegistryEntry entry : loaded) {
                add(entry);
            }
        }
    }

    public static void add(CrucibleRegistryEntry entry) {
        if (entry != null) {
            entries.put(entry.getBlock() + ":" + entry.getMeta(), entry);
        }
    }

    public static void register(Block block, int meta, int solidAmount, Fluid fluid, int fluidAmount) {
        CrucibleRegistryEntry entry = new CrucibleRegistryEntry(block, meta, solidAmount, fluid, fluidAmount);
        entries.put(block + ":" + meta, entry);
    }

    public static boolean containsItem(Block block, int meta) {
        return entries.containsKey(block + ":" + meta);
    }

    public static boolean containsFluid(Fluid fluid) {
        for (CrucibleRegistryEntry entry : entries.values()) {
            if (entry != null) {
                if (entry.getFluid() == fluid)
                    return true;
            }
        }
        
        return false;
    }

    public static CrucibleRegistryEntry getItem(Block block, int meta) {
        return entries.get(block + ":" + meta);
    }

    public static CrucibleRegistryEntry getItem(ItemStack item) {
        return getItem(Block.getBlockFromItem(item.getItem()), item.getMetadata());
    }

    public static void registerMeltables() {
        register(Blocks.COBBLESTONE, 0, 250, FluidRegistry.LAVA, 250);
        register(Blocks.STONE, 0, 250, FluidRegistry.LAVA, 250);
        register(Blocks.GRAVEL, 0, 250, FluidRegistry.LAVA, 250);
        register(Blocks.NETHERRACK, 0, 250, FluidRegistry.LAVA, 1000);
        register(Blocks.field_189877_df, 0, 250, FluidRegistry.LAVA, 2000);

        register(Blocks.SNOW, 0, 250, FluidRegistry.WATER, 250);
        register(Blocks.ICE, 0, 250, FluidRegistry.WATER, 1000);
        register(Blocks.PACKED_ICE, 0, 250, FluidRegistry.WATER, 2000);
    }
}
