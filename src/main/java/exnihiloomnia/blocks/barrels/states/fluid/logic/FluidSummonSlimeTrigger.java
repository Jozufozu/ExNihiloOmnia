package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.items.ENOItems;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidSummonSlimeTrigger extends BarrelLogic {
	public static FluidStack milk = new FluidStack(FluidRegistry.getFluid("milk"), Fluid.BUCKET_VOLUME);

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

        return fluid != null
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == FluidRegistry.WATER 
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()

				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
			    && (item.getItem() instanceof ItemBucketMilk
				|| (item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) && item.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(Fluid.BUCKET_VOLUME, false) == milk));
	}

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null  
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == FluidRegistry.WATER 
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()
				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL)
		{
			//Item milk = item.getItem();

			IFluidHandler cap = item.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

			if (cap != null && milk.equals(cap.drain(milk, true))) {
				barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, .5f, 1);
				barrel.setState(BarrelStates.SLIME_GREEN);
				return false;
			}
			/*
			else if (milk instanceof ItemBucketMilk) {

                if (player == null){
                    item.stackSize--;

                    if (item.stackSize <= 0)
                        item = null;
                }
				else if (!player.capabilities.isCreativeMode) {
					InventoryHelper.consumeItem(player, item);

					if (milk == Items.MILK_BUCKET)
						InventoryHelper.giveItemStackToPlayer(player, new ItemStack(Items.BUCKET));
					else if (milk == ENOItems.BUCKET_PORCELAIN_MILK)
						InventoryHelper.giveItemStackToPlayer(player, new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY));
				}

				barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, .5f, 1);
				barrel.setState(BarrelStates.SLIME_GREEN);

				return false;
			}
			*/
		}

		return false;
	}
}
