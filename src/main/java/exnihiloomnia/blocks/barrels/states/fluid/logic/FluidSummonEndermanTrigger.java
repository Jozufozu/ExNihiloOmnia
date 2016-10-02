package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fluids.FluidStack;

public class FluidSummonEndermanTrigger extends BarrelLogic {
	
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null  
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == ENOFluids.WITCHWATER
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL) {
			
			return item.getItem() == ENOItems.END_DOLL;
		}

		return false;
	}

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null  
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == ENOFluids.WITCHWATER
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL) {
			
			if (item.getItem() == ENOItems.END_DOLL) {
				barrel.setState(BarrelStates.ENDERMAN);

				return true;
			}
		}

		return false;
	}
}
