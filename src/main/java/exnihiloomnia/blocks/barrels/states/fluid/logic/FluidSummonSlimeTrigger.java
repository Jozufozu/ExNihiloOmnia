package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidSummonSlimeTrigger extends BarrelLogic {
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();

        return fluid != null
				&& fluid.getFluid() != null 
				&& fluid.getFluid() == FluidRegistry.WATER 
				&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()

				&& barrel.getWorld().getDifficulty() != EnumDifficulty.PEACEFUL
			    && (item.getItem() instanceof ItemBucketMilk
				|| item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null));
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
			Item milk = item.getItem();

			if (milk instanceof ItemBucketMilk) {

				if (!player.capabilities.isCreativeMode) {
					if (milk == Items.MILK_BUCKET)
						item.deserializeNBT(new ItemStack(Items.BUCKET).serializeNBT());
					else if (milk == ENOItems.BUCKET_PORCELAIN_MILK)
						item.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY).serializeNBT());
				}

				barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, .5f, 1);
				barrel.setState(BarrelStates.SLIME_GREEN);

				return false;
			}
		}

		return false;
	}
}
