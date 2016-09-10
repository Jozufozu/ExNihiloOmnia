package exnihiloomnia.blocks.barrels.states.fluid.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.InventoryHelper;

public class FluidStateLogicItems extends BarrelLogic{
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidStack ifluid = FluidContainerRegistry.getFluidForFilledItem(item);
		ItemStack full = FluidContainerRegistry.fillFluidContainer(fluid, item);

		if (fluid != null)
		{
			if (ifluid != null && barrel.fill(ifluid, false) > 0)
			{
				return true;
			}
			
			if (full != null && fluid.amount >= barrel.getCapacity())
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidStack ifluid = FluidContainerRegistry.getFluidForFilledItem(item);

		if (fluid != null )
		{
			if (ifluid != null && barrel.fill(ifluid, false) > 0)
			{
				if (player != null)
				{
					if (!player.capabilities.isCreativeMode)
					{
						if(item.stackSize > 1)
						{
							item.stackSize--;
							InventoryHelper.giveItemStackToPlayer(player, InventoryHelper.getContainer(item));
						}
						else
						{
							player.setHeldItem(hand, InventoryHelper.getContainer(item));
						}
					}
				}
				else
				{
					barrel.addOutput(InventoryHelper.getContainer(item));
				}
				
				barrel.fill(ifluid, true);
			}
			
			if (FluidContainerRegistry.isEmptyContainer(item) && fluid.amount >= barrel.getCapacity())
			{
				ItemStack full = FluidContainerRegistry.fillFluidContainer(fluid, item);
				
				if (full != null)
				{
					if (player != null)
					{
						if (!player.capabilities.isCreativeMode)
						{
							if (item.stackSize > 1) 
							{
								item.stackSize--;
								InventoryHelper.giveItemStackToPlayer(player, full);
							} 
							else 
							{
								player.setHeldItem(hand, full);
							}
						}
					}
					else
					{
						barrel.addOutput(full);
					}

					barrel.drain(barrel.getCapacity(), true);
					return true;
				}
			}
		}
		
		return false;
	}
}
