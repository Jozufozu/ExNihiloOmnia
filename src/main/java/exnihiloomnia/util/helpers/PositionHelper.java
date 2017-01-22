package exnihiloomnia.util.helpers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class PositionHelper {

	private final BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();
	private final BlockPos.MutableBlockPos random = new BlockPos.MutableBlockPos();

	public PositionHelper() {

	}

	/**
	 * Assigns a random position in a given chunk to a given {@link net.minecraft.util.math.BlockPos.MutableBlockPos}
	 * @param world the world in which you want to get a position
	 * @param chunkX the x pos of the chunk which you want to get a position from
	 * @param chunkZ the z pos of the chunk which you want to get a position from
	 * @param pos a {@link net.minecraft.util.math.BlockPos.MutableBlockPos} that will be changed to a random position in {@see chunk}
	 */
	public static void getRandomPositionInChunk(World world, int chunkX, int chunkZ, BlockPos.MutableBlockPos pos) {
		pos.setPos((chunkX * 16) + world.rand.nextInt(16), world.rand.nextInt(256), (chunkZ * 16) + world.rand.nextInt(16));
	}

	/**
	 * Gets a random position is a 3x3x3 cube centered around a given pos
	 * @param world to get a random value
	 * @param pos to get a random position around
	 * @return the random BlockPos
	 */
	public BlockPos getRandomPositionNearBlock(World world, BlockPos pos) {
		return probe.setPos(pos.getX() + (world.rand.nextInt(3) - 1), pos.getY() + (world.rand.nextInt(3) - 1), pos.getZ() + (world.rand.nextInt(3) - 1));
	}

	/**
	 * Gets a random position in a cardinal direction
	 * @param world to get a random value
	 * @param pos to get a random position around
	 * @return the random BlockPos
	 */
	public BlockPos getRandomPositionAdjacentToBlock(World world, BlockPos pos) {
		int i = world.rand.nextInt(4);
		
		switch(i) {
			case 0:
				return random.setPos(pos.getX() + 1, pos.getY(), pos.getZ());
				
			case 1:
				return random.setPos(pos.getX() - 1, pos.getY(), pos.getZ());
				
			case 2:
				return random.setPos(pos.getX(), pos.getY(), pos.getZ() + 1);
				
			case 3:
				return random.setPos(pos.getX(), pos.getY(), pos.getZ() - 1);
		}
		
		return random;
	}

	public boolean hasNearbyWaterSource(World world, BlockPos pos) {
		return world.getBlockState(getRandomPositionNearBlock(world, pos)).getBlock() == Blocks.WATER;
	}
	
	public boolean isTopBlock(World world, BlockPos pos) {
		probe.setPos(pos);
		for (int y = pos.getY() + 1; y < world.getHeight(); y++) {
			probe.setY(y);

			if (world.getBlockState(probe).getBlock() != Blocks.AIR)
				return false;
		}
		return true;
	}
	
	public boolean isRainingAt(World world, BlockPos pos) {
		Biome biome = world.getBiomeForCoordsBody(pos);
		return world.isRaining() && biome.canRain() && biome.getRainfall() > 0f && isTopBlock(world, pos);
	}
	
	public boolean canRainReach(World world, BlockPos pos) {
		return world.isRaining() && (isRainingAt(world, pos) || isRainingAt(world, getRandomPositionAdjacentToBlock(world, pos)));
	}
	
	public static boolean isChunkSpawn(World world, Chunk chunk) {
		double x = (double) world.getWorldInfo().getSpawnX() / world.provider.getMovementFactor();
		double z = (double) world.getWorldInfo().getSpawnZ() / world.provider.getMovementFactor();
		
		return getChunkContainsPoint(chunk, (int)x, (int)z);
	}
	
	public static boolean getChunkContainsPoint(Chunk chunk, int x, int z) {
    	return (x >> 4 == chunk.xPosition && z >> 4 == chunk.zPosition);
	}
	
	public static void setBlockStateWithoutReplace(World world, BlockPos pos, IBlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state);
		}
	}
	
	public int getLightLevelAbove(World world, BlockPos pos) {
		BlockPos above = pos.up();
		
		if (!world.isAirBlock(above)) {
			return 0;
		}
		else {
			return world.getLight(probe.setPos(above.getX(), above.getY() + 1, above.getZ()));
		}
	}
}
