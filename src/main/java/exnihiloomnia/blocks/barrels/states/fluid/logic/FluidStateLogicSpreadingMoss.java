package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidStateLogicSpreadingMoss extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		World world = barrel.getWorld();
		if (barrel.getFluid() != null && barrel.getFluid().getFluid() == FluidRegistry.WATER && barrel.isWooden()) {
			if (world.isRemote && world.rand.nextInt(2000000 / (barrel.getFluid().amount * BarrelStates.MOSS_SPREAD_SPEED)) == 0)
				spreadMoss(barrel);
		}
		
		return false;
	}

	private void spreadMoss(TileEntityBarrel barrel) {
		World world = barrel.getWorld();
		BlockPos pos = barrel.getPos();

		int 	x = pos.getX() + world.rand.nextInt(3) - 1,
				y = pos.getY() - 1,
				z = pos.getZ() + world.rand.nextInt(3) - 1;
		BlockPos.MutableBlockPos cobble = new BlockPos.MutableBlockPos(x, y, z);

		//if moss already exists try to spread downwards
		for (int i = 0; i <= 3; i++) {
			if (world.getBlockState(cobble) == Blocks.COBBLESTONE.getDefaultState() && world.getLight(pos, true) < 5) {
				world.setBlockState(cobble, Blocks.MOSSY_COBBLESTONE.getDefaultState());
				break;
			}
			else if (world.getBlockState(cobble) == Blocks.MOSSY_COBBLESTONE.getDefaultState()) {
				cobble.setPos(x, cobble.getY() - 1, z);
			}
			else
				break;
		}
	}
}
