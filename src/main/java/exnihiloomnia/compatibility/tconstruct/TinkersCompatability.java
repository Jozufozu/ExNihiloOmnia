package exnihiloomnia.compatibility.tconstruct;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.enums.EnumOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;

public class TinkersCompatability {
    private static int INGOT_AMOUNT = 144;

    public static void init() {
        for (EnumOre ore : ENO.oreList)
            tryRegisterOre(ore);
    }

    private static void tryRegisterOre(EnumOre ore) {
        try {
            //items
            if (ore.hasGravel())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT/4);
            
            if (ore.hasEnd())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE_ENDER, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT/4);
            
            if (ore.hasNether())
                TinkerRegistry.registerMelting(new ItemStack(ENOItems.BROKEN_ORE_NETHER, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT/4);
            
            TinkerRegistry.registerMelting(new ItemStack(ENOItems.CRUSHED_ORE, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT/4);
            TinkerRegistry.registerMelting(new ItemStack(ENOItems.POWDERED_ORE, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT/4);

            //blocks
            if (ore.hasGravel())
                TinkerRegistry.registerMelting(new ItemStack(ENOBlocks.ORE_GRAVEL, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT);
            
            if (ore.hasEnd())
                TinkerRegistry.registerMelting(new ItemStack(ENOBlocks.ORE_GRAVEL_ENDER, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT);
            
            if (ore.hasNether())
                TinkerRegistry.registerMelting(new ItemStack(ENOBlocks.ORE_GRAVEL_NETHER, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT);
            
            TinkerRegistry.registerMelting(new ItemStack(ENOBlocks.ORE_SAND, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT);
            TinkerRegistry.registerMelting(new ItemStack(ENOBlocks.ORE_DUST, 1, ore.getMetadata()), findMoltenMetal(ore), INGOT_AMOUNT);
        }
        catch (Exception e) {
            ENO.log.error("Could not add smeltery melting for: " + ore.getName().toUpperCase());
        }
    }

    private static Fluid findMoltenMetal(EnumOre ore) {
        switch (ore) {
            case IRON:
                return FluidRegistry.getFluid("iron");

            case GOLD:
                return FluidRegistry.getFluid("gold");

            case TIN:
                return FluidRegistry.getFluid("tin");

            case COPPER:
                return FluidRegistry.getFluid("copper");

            case SILVER:
                return FluidRegistry.getFluid("silver");

            case LEAD:
                return FluidRegistry.getFluid("lead");

            case NICKEL:
                return FluidRegistry.getFluid("nickel");

            case PLATINUM:
                return FluidRegistry.getFluid("platinum");

            case ALUMINUM:
                return FluidRegistry.getFluid("aluminum");
                
            case OSMIUM:
                return FluidRegistry.getFluid("osmium");
                
            case COBALT:
                return FluidRegistry.getFluid("cobalt");
                
            case ARDITE:
                return FluidRegistry.getFluid("ardite");

            default:
                return null;
        }
    }
}
