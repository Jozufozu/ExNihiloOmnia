package exnihiloomnia.blocks.barrels.states.empty.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidStateTriggerWeather extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (helper.isRainingAt(barrel.getWorld(), barrel.getPos())) {
			barrel.getFluidTank().fill(new FluidStack(FluidRegistry.WATER, 0), true);
			return true;
		}
		
		return false;
	}
}
