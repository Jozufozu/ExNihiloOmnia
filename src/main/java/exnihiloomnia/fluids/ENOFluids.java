package exnihiloomnia.fluids;

import exnihiloomnia.blocks.ENOBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ENOFluids {
	public static Fluid WITCHWATER;

	public static void register() {
	    WITCHWATER = new Fluid("witchwater", new ResourceLocation("exnihiloomnia:blocks/witchwater_still"), new ResourceLocation("exnihiloomnia:blocks/witchwater_flowing")).setBlock(ENOBlocks.WITCHWATER);

		FluidRegistry.registerFluid(WITCHWATER);

		FluidRegistry.addBucketForFluid(WITCHWATER);
	}
}
