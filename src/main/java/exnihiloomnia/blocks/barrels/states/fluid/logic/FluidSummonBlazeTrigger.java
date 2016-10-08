package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidSummonBlazeTrigger extends BarrelLogic {
	
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
        FluidStack fluid = barrel.getFluid();

        return fluid != null && fluid.getFluid() != null && fluid.getFluid() == FluidRegistry.LAVA && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity() && barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL && item.getItem() == ENOItems.BLAZE_DOLL;

    }

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null  
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == FluidRegistry.LAVA
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL) {
			
			if (item.getItem() == ENOItems.BLAZE_DOLL) {
				barrel.setState(BarrelStates.BLAZE);

				return true;
			}
		}

		return false;
	}
}
