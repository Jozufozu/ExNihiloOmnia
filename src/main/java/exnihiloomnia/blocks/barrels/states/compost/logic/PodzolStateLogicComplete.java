package exnihiloomnia.blocks.barrels.states.compost.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class PodzolStateLogicComplete extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() >= 1.0d) {
			barrel.setState(BarrelStates.OUTPUT);
			barrel.setContents(new ItemStack(Blocks.DIRT, 1, 2));
			
			return true;
		}
		
		return false;
	}
}
