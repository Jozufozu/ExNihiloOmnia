package exnihiloomnia.registries.ore;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.compatibility.ENOOres;
import exnihiloomnia.items.ENOItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class OreRegistry {

    public static List<OreRegistryEntry> registry = new ArrayList<OreRegistryEntry>();
    private static List<OreRegistryEntry> defaults = new ArrayList<OreRegistryEntry>();

    public void initialize() {
        for (OreRegistryEntry ore : defaults) {
            if (OreDictionary.getOres(ore.getOreDictName("ore")).size() > 0 || ENOOres.force_copper)
                registry.add(ore);
        }
    }

    public static void addCrafting() {
        for (OreRegistryEntry ore : registry) {
            int meta = metaLookup(ore);

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, meta),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE, 1, meta)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_SAND, 1, meta),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.CRUSHED_ORE, 1, meta)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_DUST, 1, meta),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.POWDERED_ORE, 1, meta)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, meta),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE_NETHER, 1, meta)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, meta),
                    "xx",
                    "xx",
                    'x', new ItemStack(ENOItems.BROKEN_ORE_ENDER, 1, meta)));
        }
    }

    public static void addSmelting() {
        for (OreRegistryEntry ore : registry) {
            int meta = metaLookup(ore);
            ItemStack ingot = new ItemStack(ENOItems.INGOT_ORE, 1, meta);

            if (!ore.hasIngot()) {
                ItemStack oredictIngot = OreDictionary.getOres(ore.getOreDictName("ingot")).get(0);

                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, meta), oredictIngot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_SAND, 1, meta), oredictIngot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_DUST, 1, meta), oredictIngot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, meta), oredictIngot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, meta), oredictIngot, 0);
            }
            else {
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, meta), ingot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_SAND, 1, meta), ingot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_DUST, 1, meta), ingot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, meta), ingot, 0);
                GameRegistry.addSmelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, meta), ingot, 0);
            }
        }
    }

    public static int metaLookup(OreRegistryEntry entry) {
        return registry.indexOf(entry);
    }
}
