package exnihiloomnia.compatibility.appliedenergistics;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AE2 {

    public static BarrelLogic SKYSTONE = new FluidCraftSkystoneTrigger();

    public static void initialize() {
        BarrelStates.FLUID.addLogic(SKYSTONE);

        SieveRegistryEntry certusQuartz = new SieveRegistryEntry(Blocks.SAND.getDefaultState(), EnumMetadataBehavior.IGNORED);
        certusQuartz.addReward(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("appliedenergistics2", "material")), 1, 0), 5);
        certusQuartz.addReward(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("appliedenergistics2", "material")), 1, 1), 1);

        SieveRegistryEntry dust = new SieveRegistryEntry(ENOBlocks.DUST.getDefaultState(), EnumMetadataBehavior.IGNORED);
        dust.addReward(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("appliedenergistics2", "material")), 1, 2), 9);

        SieveRegistry.add(certusQuartz);
        SieveRegistry.add(dust);
    }
}
