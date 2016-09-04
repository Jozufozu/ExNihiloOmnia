package exnihiloomnia.blocks.barrels.states.dolls.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.EnumDifficulty;

public class EndStateLogic extends BarrelLogic{
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() == -1)
		{
			barrel.startTimer(1000);
		}

		if (barrel.getTimerStatus() >= 1.0d 
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
				&& barrel.getWorld().isAirBlock(barrel.getPos().up()))
		{
			if (!barrel.getWorld().isRemote)
			{
				EntityEnderman enderman = new EntityEnderman(barrel.getWorld());
				enderman.setPositionAndRotation(barrel.getPos().getX() + 0.5d, barrel.getPos().getY() + 1.25d, barrel.getPos().getZ() + 0.5d, barrel.getWorld().rand.nextFloat() * 360f, 0.0f);

				barrel.getWorld().spawnEntityInWorld(enderman);
			}
			
			barrel.setState(BarrelStates.EMPTY);
			
			return true;
		}
		
		return false;
	}
}
