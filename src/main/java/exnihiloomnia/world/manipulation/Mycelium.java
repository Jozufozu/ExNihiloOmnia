package exnihiloomnia.world.manipulation;

import java.util.Random;

import exnihiloomnia.util.helpers.PositionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Mycelium {
	public static final int DEFAULT_GROWTH_SPEED = 8;
	private static int growth = 0;
	private static boolean rain_reactive;
	
	private static BlockPos pos = null;
	private static IBlockState state = null;
	private static Random rng = new Random();
	
	public static int getGrowth() {
		return growth;
	}

	public static void setGrowth(int growth) {
		Mycelium.growth = growth;
	}
	
	public static boolean getSpreadsWhileRaining() {
		return rain_reactive;
	}

	public static void setSpreadsWhileRaining(boolean spreads) {
		Mycelium.rain_reactive = spreads;
	}
	
	public static void grow(World world, Chunk chunk) {		
		for (int i = 0; i < growth; i++) {
			pos = PositionHelper.getRandomPositionInChunk(world, chunk);
			state = world.getBlockState(pos);
			
			if (state.getBlock() == Blocks.MYCELIUM && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
				if (PositionHelper.hasNearbyWaterSource(world, pos)) {
					spawnRandomMushroom(world, pos);
				}
				else if (getSpreadsWhileRaining() && PositionHelper.isRainingAt(world, pos)) {
					rng.setSeed(pos.getX() * pos.getZ());
					rng.nextDouble(); //The first value is not very random at all. Skip that.

					if (rng.nextInt(12) == 0) {
						spawnRandomMushroom(world, pos);
					}
				}
			}
		}
	}

	private static void spawnRandomMushroom(World world, BlockPos pos) {
		switch (world.rand.nextInt(2)) {
			case 0:
				world.setBlockState(pos.up(), Blocks.BROWN_MUSHROOM.getDefaultState());
				break;
	
			case 1:
				world.setBlockState(pos.up(), Blocks.RED_MUSHROOM.getDefaultState());
				break;
		}
	}
}
