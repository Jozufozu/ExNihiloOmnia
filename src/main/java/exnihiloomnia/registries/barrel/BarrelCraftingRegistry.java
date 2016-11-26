package exnihiloomnia.registries.barrel;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.compatibility.forestry.ForestryCompatibility;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.IRegistry;
import exnihiloomnia.registries.barrel.files.BarrelCraftingRecipeLoader;
import forestry.core.fluids.Fluids;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class BarrelCraftingRegistry implements IRegistry<BarrelCraftingTrigger> {
    public static final BarrelCraftingRegistry INSTANCE = new BarrelCraftingRegistry();


    public static HashMap<String, BarrelCraftingTrigger> entries = new HashMap<String, BarrelCraftingTrigger>();
    public static String path = ENO.path + File.separator + "registries" + File.separator + "barrel" + File.separator;

    @Override
    public void initialize() {
        clear();

        loadDefaults();

        if (ENORegistries.dumpRegistries)
            BarrelCraftingRecipeLoader.dumpRecipes(entries, path);

        List<BarrelCraftingTrigger> loaded = BarrelCraftingRecipeLoader.load(path);

        if (loaded != null && !loaded.isEmpty()) {
            for (BarrelCraftingTrigger entry : loaded) {
                add(entry);
            }
        }
    }

    @Override
    public HashMap<String, BarrelCraftingTrigger> getEntries() {
        return entries;
    }

    @Override
    public void clear() {
        entries = new HashMap<String, BarrelCraftingTrigger>();
    }

    public static void add(BarrelCraftingTrigger recipe) {
        entries.put(recipe.getKey(), recipe);
    }

    public static void loadDefaults() {
        if (BarrelStates.ALLOW_CRAFTING_NETHERRACK)
            add(new BarrelCraftingTrigger(FluidRegistry.LAVA, new ItemStack(Items.REDSTONE), new ItemStack(Blocks.NETHERRACK)));

        if (BarrelStates.ALLOW_CRAFTING_CHORUS_FRUIT)
            add(new BarrelCraftingTrigger(ENOFluids.WITCHWATER, new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.CHORUS_FRUIT)));

        if (BarrelStates.ALLOW_CRAFTING_END_STONE)
            add(new BarrelCraftingTrigger(FluidRegistry.LAVA, new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Blocks.END_STONE)));

        if (BarrelStates.ALLOW_CRAFTING_CLAY)
            add(new BarrelCraftingTrigger(FluidRegistry.WATER, new ItemStack(ENOBlocks.DUST), new ItemStack(Blocks.CLAY)));

        if (BarrelStates.ALLOW_CRAFTING_SOUL_SAND)
            add(new BarrelCraftingTrigger(ENOFluids.WITCHWATER, new ItemStack(Blocks.SAND), new ItemStack(Blocks.SOUL_SAND)));

        if (Loader.isModLoaded("forestry"))
            add(new BarrelCraftingTrigger(Fluids.SEED_OIL.getFluid(), new ItemStack(ForestryCompatibility.BEE_TRAP), new ItemStack(ForestryCompatibility.BEE_TRAP_TREATED)));

        if (Loader.isModLoaded("appliedenergistics2"))
            add(new BarrelCraftingTrigger(FluidRegistry.LAVA, new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("appliedenergistics2", "material")), 1, 2), new ItemStack(Block.REGISTRY.getObject(new ResourceLocation("appliedenergistics2", "sky_stone_block")))));
    }
}
