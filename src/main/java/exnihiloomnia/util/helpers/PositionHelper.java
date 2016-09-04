package exnihiloomnia.util.helpers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class PositionHelper {
	public static BlockPos getRandomPositionInChunk(World world, Chunk chunk)
	{
		return new BlockPos((chunk.xPosition * 16) + world.rand.nextInt(16), world.rand.nextInt(256), (chunk.zPosition * 16) + world.rand.nextInt(16));
	}
	
	public static BlockPos getRandomPositionNearBlock(World world, BlockPos pos)
	{
		return new BlockPos(pos.getX() + (world.rand.nextInt(3) - 1), pos.getY() + (world.rand.nextInt(3) - 1), pos.getZ() + (world.rand.nextInt(3) - 1));
	}
	
	public static BlockPos getRandomPositionAdjacentToBlock(World world, BlockPos pos)
	{
		int i = world.rand.nextInt(4);
		
		switch(i)
		{
		case 0:
			return new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
			
		case 1:
			return new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
			
		case 2:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
			
		case 3:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
		}
		
		return pos;
	}

	public static void dropItemInWorld(BlockPos pos, EntityPlayer player, ItemStack stack, double speedfactor)
	{

		EntityItem droppedEntity = new EntityItem(player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
		droppedEntity.setDefaultPickupDelay();
		player.getEntityWorld().spawnEntityInWorld(droppedEntity);
	}
	
	public static boolean hasNearbyWaterSource(World world, BlockPos pos)
	{
		return world.getBlockState(getRandomPositionNearBlock(world, pos)).getBlock() == Blocks.WATER;
	}
	
	public static boolean isTopBlock(World world, BlockPos pos)
	{
		for (int y = world.getChunkFromBlockCoords(pos).getTopFilledSegment() + 16; y >= pos.getY(); y--)
		{
			if (world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock() != Blocks.AIR)
			{
				if (y > pos.getY())
				{
					return false;
				}				
				else if (y == pos.getY())
				{
					return true;
				}
			}
		}
		
		//There are no blocks above the current position. It technically counts.
		return true;
	}
	
	public static boolean isRainingAt(World world, BlockPos pos)
	{
		return world.isRaining() && world.getBiomeGenForCoords(pos).getRainfall() > 0f && isTopBlock(world, pos);
	}
	
	public static boolean canRainReach(World world, BlockPos pos)
	{
		if (world.isRaining())
		{
			if (isRainingAt(world, pos))
			{
				return true;
			}

			else
			{
				if (isRainingAt(world, getRandomPositionAdjacentToBlock(world, pos)))
				{
					return true;
				}

			}
		}

		return false;
	}
	
	public static boolean isChunkSpawn(World world, Chunk chunk)
	{
		double x = (double)world.getWorldInfo().getSpawnX() / world.provider.getMovementFactor();
		double z = (double)world.getWorldInfo().getSpawnZ() / world.provider.getMovementFactor();
		
		return getChunkContainsPoint(chunk, (int)x, (int)z);
	}
	
	public static boolean getChunkContainsPoint(Chunk chunk, int x, int z)
	{
    	return (x >> 4 == chunk.xPosition && z >> 4 == chunk.zPosition);
	}
	
	public static void setBlockStateWithoutReplace(World world, BlockPos pos, IBlockState state)
	{
		if (world.isAirBlock(pos))
		{
			world.setBlockState(pos, state);
		}
	}
	
	public static int getLightLevelAbove(World world, BlockPos pos)
	{
		BlockPos above = pos.up();
		
		if (!world.isAirBlock(above))
		{
			return 0;
		}
		else
		{
			return world.getLight(new BlockPos(above.getX(), above.getY() + 1, above.getZ()));
		}
	}
}
