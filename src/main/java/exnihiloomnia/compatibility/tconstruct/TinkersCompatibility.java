package exnihiloomnia.compatibility.tconstruct;

import exnihiloomnia.ENO;
import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.compatibility.tconstruct.modifiers.ModCrooked;
import exnihiloomnia.compatibility.tconstruct.modifiers.ModHammered;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;

import javax.annotation.Nullable;

public class TinkersCompatibility {

    public static Modifier modCrooked;
    public static Modifier modHammered;

    private static int INGOT_AMOUNT = 144;

    public static void initilize() {
        if (ENOCompatibility.add_tcon_modifiers) {
            registerModifiers();
            MinecraftForge.EVENT_BUS.register(new CrookedToolEventHandler());
        }

        if (ENOCompatibility.add_smeltery_melting) {
            for (Ore ore : OreRegistry.registry.values())
                tryRegisterOre(ore);
        }
    }

    private static void registerModifiers() {
        modCrooked = registerModifier(new ModCrooked());
        modCrooked.addItem(ENOItems.CROOK_BONE);

        modHammered = registerModifier(new ModHammered());
        modHammered.addItem(ENOItems.HAMMER_DIAMOND);
    }

    private static  <T extends IModifier> T registerModifier(T modifier) {
        TinkerRegistry.registerModifier(modifier);
        return modifier;
    }

    private static void tryRegisterOre(Ore ore) {
        try {
            Fluid metal = findMoltenMetal(ore);

            //items
            if (ore.hasGravel())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE, 1, ore.getMetadata()), metal, INGOT_AMOUNT/4);
            
            if (ore.hasEnd())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE_ENDER, 1, ore.getMetadata()), metal, INGOT_AMOUNT/4);
            
            if (ore.hasNether())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE_NETHER, 1, ore.getMetadata()), metal, INGOT_AMOUNT/4);
            
            TinkerRegistry.registerMelting(new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata()), metal, INGOT_AMOUNT/4);
            TinkerRegistry.registerMelting(new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata()), metal, INGOT_AMOUNT/4);

            //blocks
            Block oreBlock = ore.getBlock();
            for (EnumOreBlockType type : EnumOreBlockType.values())
                if (ore.hasType(type)) TinkerRegistry.registerMelting(new ItemStack(oreBlock, 1, type.ordinal()), metal, INGOT_AMOUNT);
        }
        catch (Exception e) {
            ENO.log.error("Could not add smeltery melting for: " + ore.getName().toUpperCase());
        }
    }

    @Nullable
    private static Fluid findMoltenMetal(Ore ore) {
        return FluidRegistry.getFluid(ore.getName());
    }
}
