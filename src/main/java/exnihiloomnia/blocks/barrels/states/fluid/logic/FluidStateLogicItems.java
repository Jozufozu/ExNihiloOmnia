package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidStateLogicItems extends BarrelLogic {
	private static final ResourceLocation wateringCan = new ResourceLocation("extrautils2", "WateringCan");

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidStack ifluid = FluidContainerRegistry.getFluidForFilledItem(item);
		ItemStack full = FluidContainerRegistry.fillFluidContainer(fluid, item);

		if (fluid != null) {
			if (ifluid != null && barrel.getFluidTank().fill(ifluid, false) > 0)
				return true;
			
			if (full != null && fluid.amount >= barrel.getFluidTank().getCapacity())
				return true;

			if (item.getItem().getRegistryName().equals(new ResourceLocation("extrautils2", "WateringCan")))
				return true;
		}
		
		return false;
	}

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidStack ifluid = FluidContainerRegistry.getFluidForFilledItem(item);

		if (fluid != null ) {
			//watering can!
			if (item.getItem().getRegistryName().equals(wateringCan)) {
				if (fluid.amount > 0 && item.getItemDamage() > 0) {
					FluidStack out = barrel.getFluidTank().drain(item.getItemDamage(), true);
					if (out != null)
						item.setItemDamage(item.getItemDamage() - out.amount);
				}
			}

			if (item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
				FluidUtil.interactWithFluidHandler(item, barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), player);
			}
			else if (FluidContainerRegistry.isEmptyContainer(item) && fluid.amount >= barrel.getFluidTank().getCapacity()) {
				ItemStack full = FluidContainerRegistry.fillFluidContainer(fluid, item);

				if (full != null) {
					if (player != null) {
						if (!player.capabilities.isCreativeMode) {
							if (item.stackSize > 1) {
								item.stackSize--;
								InventoryHelper.giveItemStackToPlayer(player, full);
							}
							else
								player.setHeldItem(hand, full);
						}
					}
					else {
						InventoryHelper.consumeItem(null, item);

						barrel.addOutput(full);
					}

					barrel.getFluidTank().drain(barrel.getFluidTank().getCapacity(), true);
				}
			}
			else if (ifluid != null && barrel.getFluidTank().fill(ifluid, false) > 0) {
				if (player != null) {
					if (!player.capabilities.isCreativeMode)
						item.setItem(InventoryHelper.getContainer(item).getItem());
				}
				else {
					InventoryHelper.consumeItem(null, item);
					barrel.addOutput(InventoryHelper.getContainer(item));
				}
				
				barrel.getFluidTank().fill(ifluid, true);
			}
		}
		
		return false;
	}
}
