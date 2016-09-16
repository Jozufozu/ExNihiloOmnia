package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.PositionHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidStateLogicRain extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getFluid() != null && barrel.getFluid().getFluid() != null) {
			if (PositionHelper.isRainingAt(barrel.getWorld(), barrel.getPos())) {
				barrel.getFluidTank().fill(new FluidStack(FluidRegistry.WATER, 1), true);
			}
		}
		
		return false;
	}
}
