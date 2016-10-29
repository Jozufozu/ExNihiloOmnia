package exnihiloomnia.blocks.barrels.states.output.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.PositionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OutputStateLogicGrowingMycelium extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel)  {
		if (!barrel.getWorld().isRemote
				&& barrel.getContents().getItem() == Item.getItemFromBlock(Blocks.DIRT)
				&& barrel.getContents().getMetadata() == 0) {
			
			if (barrel.getWorld().rand.nextInt(1000) == 0) {
				if (isRandomNearbyBlockMycelium(barrel)
					&& helper.getLightLevelAbove(barrel.getWorld(), barrel.getPos()) >= 9) {
					barrel.setContents(new ItemStack(Blocks.MYCELIUM, 1));
				}
			}
		}
		
		return false;
	}
	
	private static boolean isRandomNearbyBlockMycelium(TileEntityBarrel barrel) {
		World world = barrel.getWorld();
		BlockPos pos = barrel.getPos();

		int x = world.rand.nextInt(3) - 1;
		int y = world.rand.nextInt(3) - 1;
		int z = world.rand.nextInt(3) - 1;

        return world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock() == Blocks.MYCELIUM;
    }
	
	private static int getLightLevel(TileEntityBarrel barrel) {
		return barrel.getWorld().getLight(new BlockPos(barrel.getPos().getX(), barrel.getPos().getY() + 1, barrel.getPos().getZ()));
	}
}
