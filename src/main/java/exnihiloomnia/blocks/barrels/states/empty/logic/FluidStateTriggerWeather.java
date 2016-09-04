package exnihiloomnia.blocks.barrels.states.empty.logic;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.PositionHelper;

public class FluidStateTriggerWeather extends BarrelLogic {
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {

		if (PositionHelper.isRainingAt(barrel.getWorld(), barrel.getPos()))
		{
			barrel.fill(new FluidStack(FluidRegistry.WATER, 0), true);
			return true;
		}
		
		return false;
	}
}
