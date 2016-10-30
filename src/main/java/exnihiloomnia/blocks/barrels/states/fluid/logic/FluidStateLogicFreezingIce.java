package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidStateLogicFreezingIce extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getFluid() != null) {
			if (barrel.getFluid().getFluid() == FluidRegistry.WATER && barrel.getFluid().amount == barrel.getFluidTank().getCapacity()
					&& !barrel.getWorld().isRemote
					&& barrel.getWorld().getBiomeForCoordsBody(barrel.getPos()).getTempCategory().equals(Biome.TempCategory.COLD)) {

				if (barrel.getWorld().rand.nextInt(1000) == 0) {
					barrel.setState(BarrelStates.OUTPUT);
					barrel.setContents(new ItemStack(Blocks.ICE));
				}
			}
		}
		return false;
	}
}
