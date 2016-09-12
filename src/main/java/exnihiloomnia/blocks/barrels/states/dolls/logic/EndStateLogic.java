package exnihiloomnia.blocks.barrels.states.dolls.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EndStateLogic extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		World world = barrel.getWorld();
		BlockPos pos = barrel.getPos();

		if (barrel.getTimerStatus() == -1) {
			barrel.startTimer(1000);
		}
		
		if (barrel.getTimerStatus() >= 1.0d
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
				&& barrel.getWorld().isAirBlock(barrel.getPos().up())) {
			if (!barrel.getWorld().isRemote) {
				EntityEnderman blaze = new EntityEnderman(world);
				blaze.setPositionAndRotation(pos.getX() + 0.5d, pos.getY() + 1.25d, pos.getZ() + 0.5d, world.rand.nextFloat() * 360f, 0.0f);

				barrel.getWorld().spawnEntityInWorld(blaze);
			}

			barrel.setState(BarrelStates.EMPTY);

			return true;
		}

		return false;
	}
}
