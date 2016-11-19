package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.fluids.ENOFluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class FluidCraftChorusTrigger extends BarrelLogic {

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
        return item != null && item.getItem() == Items.GOLDEN_APPLE
                && barrel.getFluid().getFluid() == ENOFluids.WITCHWATER
                && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity();
    }

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		if (item.getItem() == Items.GOLDEN_APPLE
		    && barrel.getFluid().getFluid() == ENOFluids.WITCHWATER
		    && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()) {
			
			barrel.setState(BarrelStates.OUTPUT);
			barrel.setContents(new ItemStack(Items.CHORUS_FRUIT, 1));

			barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 4.5f);
			
			return true;
		}
		
		return false;
	}
}
