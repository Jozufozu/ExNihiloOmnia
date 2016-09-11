package exnihiloomnia.blocks.barrels.states.fluid.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fluids.FluidRegistry;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;

public class FluidCraftNetherrackTrigger extends BarrelLogic{

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {

        return item.getItem() == Items.REDSTONE
                && barrel.getFluid().getFluid() == FluidRegistry.LAVA
                && barrel.getFluidAmount() == barrel.getCapacity();

    }

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		if (item.getItem() == Items.REDSTONE
		    && barrel.getFluid().getFluid() == FluidRegistry.LAVA
		    && barrel.getFluidAmount() == barrel.getCapacity())
		{
			barrel.setState(BarrelStates.OUTPUT);
			barrel.setContents(new ItemStack(Blocks.NETHERRACK, 1));
			consumeItem(player, item);
			
			barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 4.5f);
			
			return true;
		}
		
		return false;
	}
	
}
