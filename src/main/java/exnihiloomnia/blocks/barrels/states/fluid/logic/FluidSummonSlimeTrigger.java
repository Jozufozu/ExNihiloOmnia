package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class FluidSummonSlimeTrigger extends BarrelLogic {
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

        return fluid != null
				&& fluid.getFluid() == FluidRegistry.WATER
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
				&& (item.getItem() == Items.MILK_BUCKET
				|| item.getItem() == ENOItems.BUCKET_PORCELAIN_MILK
				|| item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null));
	}

	@Override
	public boolean onUseItem(@Nullable EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null
				&& fluid.getFluid() == FluidRegistry.WATER
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
				&& (item.getItem() == Items.MILK_BUCKET
				|| item.getItem() == ENOItems.BUCKET_PORCELAIN_MILK
				|| item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)))
		{
			IFluidHandler handler = item.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

			FluidStack drain = handler.drain(1000, false);

			if (drain != null) {
				if ("milk".equals(drain.getFluid().getName()) && drain.amount == 1000) {

					if (player == null || !player.isCreative()) {
						handler.drain(1000, true);

						if (player == null) {
							barrel.addOutput(item.copy());
							item.stackSize--;
						}
					}

					barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, .5f, 1);
					barrel.setState(BarrelStates.SLIME_GREEN);
				}
			}
			else if (item.getItem() == Items.MILK_BUCKET || item.getItem() == ENOItems.BUCKET_PORCELAIN_MILK) {
				ItemStack ret = null;

				if (item.getItem() == Items.MILK_BUCKET)
					ret = new ItemStack(Items.BUCKET);
				if (item.getItem() == ENOItems.BUCKET_PORCELAIN_MILK)
					ret = new ItemStack(ENOItems.BUCKET_PORCELAIN);

				if (player == null) {
					item.stackSize--;
					barrel.addOutput(ret);
				}
				else if (!player.isCreative()) {
					item.stackSize--;
					InventoryHelper.giveItemStackToPlayer(player, ret);
				}

				barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, .5f, 1);
				barrel.setState(BarrelStates.SLIME_GREEN);
			}
		}

		return false;
	}
}
