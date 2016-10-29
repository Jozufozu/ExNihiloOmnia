package exnihiloomnia.world.manipulation;

import exnihiloomnia.util.helpers.PositionHelper;
import exnihiloomnia.world.ENOWorld;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Moss {
	public static final int DEFAULT_GROWTH_SPEED = 8;
	private static int growth = 0;
	private static boolean rain_reactive;

	private static PositionHelper helper = new PositionHelper();
	private static BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	private static IBlockState state;

	public static int getGrowth() {
		return growth;
	}

	public static void setGrowth(int growth) {
		Moss.growth = growth;
	}
	
	public static boolean getSpreadsWhileRaining() {
		return rain_reactive;
	}

	public static void setSpreadsWhileRaining(boolean spreads) {
		Moss.rain_reactive = spreads;
	}

	public static void grow(World world, Chunk chunk) {

		for (int i = 0; i < growth; i++) {
			PositionHelper.getRandomPositionInChunk(world, chunk, pos);
			state = world.getBlockState(pos);

			if (state.getBlock() == Blocks.AIR)
				continue;

			if (isValid() && (world.getLight(pos, true) < ENOWorld.getMossLightLevel() && ((helper.hasNearbyWaterSource(world, pos) && ENOWorld.mossSpreadsNearWater()) || (getSpreadsWhileRaining() && helper.canRainReach(world, pos))))) {

				if (isValidCobblestone())
					world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
				else if (isValidStoneBrick())
					world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
				else if (isValidCobbleWall())
					world.setBlockState(pos, Blocks.COBBLESTONE_WALL.getDefaultState().withProperty(BlockWall.VARIANT, BlockWall.EnumType.MOSSY));
			}
		}
	}
	
	private static boolean isValidCobblestone() {
		return state.getBlock() == Blocks.COBBLESTONE;
	}
	
	private static boolean isValidStoneBrick() {
		return state.getBlock() == Blocks.STONEBRICK
				&& (state.getValue(BlockStoneBrick.VARIANT) == BlockStoneBrick.EnumType.DEFAULT
				|| state.getValue(BlockStoneBrick.VARIANT) == BlockStoneBrick.EnumType.CRACKED);
	}
	
	private static boolean isValidCobbleWall() {
		return state.getBlock() == Blocks.COBBLESTONE_WALL
				&& (state.getValue(BlockWall.VARIANT) == BlockWall.EnumType.NORMAL);
	}

	private static boolean isValid() {
		return isValidCobblestone() || isValidCobbleWall() || isValidStoneBrick();
	}
}
