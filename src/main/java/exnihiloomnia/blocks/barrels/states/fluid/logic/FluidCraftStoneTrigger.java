package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidCraftStoneTrigger extends BarrelLogic {

	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getFluid() != null && barrel.getFluid().getFluid() != null) {
			if (barrel.getFluid().getFluid().equals(FluidRegistry.WATER)) {
				IBlockState up = barrel.getWorld().getBlockState(barrel.getPos().up());
				
				if (up.getBlock() == Blocks.LAVA && up.getBlock().getMetaFromState(up) == 0) {
					barrel.setContents(new ItemStack(Blocks.STONE, 1));
					barrel.setState(BarrelStates.OUTPUT);
					
					barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 4.5f);
					
					return true;
				}
				
				if (up.getBlock() == Blocks.LAVA && up.getBlock().getMetaFromState(up) > 0) {
					barrel.setContents(new ItemStack(Blocks.COBBLESTONE, 1));
					barrel.setState(BarrelStates.OUTPUT);
					
					barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 4.5f);
					
					return true;
				}
			}
		}
		
		return false;
	}
}
