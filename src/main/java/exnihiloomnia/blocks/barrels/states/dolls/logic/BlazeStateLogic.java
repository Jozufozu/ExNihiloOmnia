package exnihiloomnia.blocks.barrels.states.dolls.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class BlazeStateLogic extends BarrelLogic{
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		World world = barrel.getWorld();
		BlockPos pos = barrel.getPos();

		if (barrel.getTimerStatus() == -1)
		{
			barrel.startTimer(1000);
		}
		if (barrel.getTimerTime() % 30 == 0)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)pos.getX() + Math.random(), (double)pos.getY() + 1.2D, (double)pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
		}

		if (barrel.getTimerTime() % 5 == 0)
		{
			world.spawnParticle(EnumParticleTypes.LAVA, (double)pos.getX() + Math.random(), (double)pos.getY() + 1.2D, (double)pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
		}
		if (barrel.getTimerStatus() >= 1.0d 
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
				&& barrel.getWorld().isAirBlock(barrel.getPos().up()))
		{
			if (!barrel.getWorld().isRemote)
			{
				EntityBlaze blaze = new EntityBlaze(barrel.getWorld());
				blaze.setPositionAndRotation(barrel.getPos().getX() + 0.5d, barrel.getPos().getY() + 1.25d, barrel.getPos().getZ() + 0.5d, barrel.getWorld().rand.nextFloat() * 360f, 0.0f);

				barrel.getWorld().spawnEntityInWorld(blaze);
			}
			
			barrel.setState(BarrelStates.EMPTY);
			
			return true;
		}
		
		return false;
	}
}
