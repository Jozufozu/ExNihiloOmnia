package exnihiloomnia.blocks.barrels.states.compost.logic;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;

public class CoarseDirtStateLogicComplete extends BarrelLogic{

	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() >= 1.0d)
		{
			barrel.setState(BarrelStates.OUTPUT);
			barrel.setContents(new ItemStack(Blocks.DIRT, 1, 1));
			
			return true;
		}
		
		return false;
	}
}
