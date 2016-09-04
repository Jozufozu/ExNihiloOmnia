package exnihiloomnia.blocks.barrels.states.empty.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.Color;

public class EmptyStateLogic extends BarrelLogic{
	@Override
	public boolean onActivate(TileEntityBarrel barrel) {
		barrel.drain(barrel.getCapacity(), true);
		barrel.setContents(null);
		barrel.setColor(new Color("FFFFFF"));
		barrel.setVolume(0);
		barrel.resetTimer();
		
		return false;
	}
}
