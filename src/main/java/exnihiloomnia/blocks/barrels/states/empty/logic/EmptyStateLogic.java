package exnihiloomnia.blocks.barrels.states.empty.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.Color;

public class EmptyStateLogic extends BarrelLogic {
	
	@Override
	public boolean onActivate(TileEntityBarrel barrel) {
		barrel.getFluidTank().drain(barrel.getFluidTank().getCapacity(), false);
		barrel.setContents(null);
		barrel.setColor(Color.WHITE);
		barrel.setVolume(0);
		barrel.resetTimer();
		
		return false;
	}
}
