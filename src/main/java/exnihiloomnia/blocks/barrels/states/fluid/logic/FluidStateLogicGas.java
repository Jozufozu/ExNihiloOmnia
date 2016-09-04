package exnihiloomnia.blocks.barrels.states.fluid.logic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;

public class FluidStateLogicGas extends BarrelLogic{
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) 
	{
		if (barrel.getFluid() != null && barrel.getFluid().getFluid() != null)
		{
			//if the FLUID is gaseous...
			if(barrel.getFluid().getFluid().isGaseous())
			{
				World world = barrel.getWorld();
				
				//and the space above the barrel is EMPTY...
				BlockPos pos = barrel.getPos();
				BlockPos above = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
				
				if(world.isAirBlock(above))
				{
					//float free little cloud dude!
					Block fblock = barrel.getFluid().getFluid().getBlock();
					
					world.setBlockState(above, fblock.getDefaultState(), 3);
					world.notifyBlockOfStateChange(above, fblock);
					
					barrel.drain(barrel.getCapacity(), true);
					
					return true;
				}
			}
		}
		
		return false;
	}
}
