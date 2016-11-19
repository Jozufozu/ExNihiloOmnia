package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidTransformWitchwater extends BarrelLogic {

	private BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
        return item.getItem() == ENOItems.SPORES
                && barrel.getFluid().getFluid() == FluidRegistry.WATER
                && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity();
    }

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		if (item.getItem() == ENOItems.SPORES
		    && barrel.getFluid().getFluid() == FluidRegistry.WATER
		    && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()) {
			
			barrel.setState(BarrelStates.TRANSFORM_WITCHWATER);

			barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_BOBBER_SPLASH, SoundCategory.BLOCKS, 0.12f, 4.5f);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		World world = barrel.getWorld();

		if (!world.isRemote) {
			if (world.rand.nextInt(1000) == 0
					&& barrel.isWooden()
					&& isOnTopOfWitchwater(barrel)
					&& barrel.getFluid().getFluid() == FluidRegistry.WATER
					&& barrel.getFluid().amount == barrel.getFluidTank().getCapacity()) {

				barrel.setState(BarrelStates.TRANSFORM_WITCHWATER);

				return true;
			}
		}

		return false;
	}

	public boolean isOnTopOfWitchwater(TileEntityBarrel barrel) {

		BlockPos pos = barrel.getPos();
		probe.setPos(pos);

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				probe.setPos(pos.getX() + x, pos.getY() - 1, pos.getZ() + z);
				if (barrel.getWorld().getBlockState(probe).getBlock() == Blocks.MYCELIUM)
					return true;
			}
		}

		return false;
	}
}
