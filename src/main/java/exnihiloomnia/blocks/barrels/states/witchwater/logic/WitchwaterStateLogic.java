package exnihiloomnia.blocks.barrels.states.witchwater.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.fluids.ENOFluids;
import net.minecraftforge.fluids.FluidStack;

public class WitchwaterStateLogic extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() == -1) {
			barrel.startTimer(1000);
		}

		if (barrel.getTimerStatus() >= 1.0d) {
			if (!barrel.getWorld().isRemote) {
				barrel.transformFluidTo(new FluidStack(ENOFluids.WITCHWATER, 1000));
				barrel.setState(BarrelStates.FLUID);
				barrel.resetTimer();
			}
			
			return true;
		}
		
		return false;
	}
}
