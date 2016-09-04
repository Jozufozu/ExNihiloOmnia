package exnihiloomnia.blocks.barrels.states.empty;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;

//Does nothing. Renders nothing. Is simply a container for triggers.
public class BarrelStateEmpty extends BarrelState{
	private static String[] description = {"Empty"};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.EMPTY";
	}

	@Override
	public boolean canManipulateFluids(TileEntityBarrel barrel) {
		return true;
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel)
	{
		return description;
	}
}
