package exnihiloomnia.blocks.barrels.states.empty;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.client.resources.I18n;

//Does nothing. Renders nothing. Is simply a container for triggers.
public class BarrelStateEmpty extends BarrelState {
	private static String[] description = {I18n.format("exnihiloomnia.info.barrel.empty")};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.EMPTY";
	}

	@Override
	public boolean canManipulateFluids(TileEntityBarrel barrel) {
		return true;
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		return description;
	}
}
