package exnihiloomnia.compatibility.appliedenergistics;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidCraftSkystoneTrigger extends BarrelLogic {

	private static ResourceLocation skyStone = new ResourceLocation("appliedenergistics2", "sky_stone_block");
	private static ResourceLocation dust = new ResourceLocation("appliedenergistics2", "material");


	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
        return item != null && item.getItem() == Item.REGISTRY.getObject(dust)
				&& item.getMetadata() == 2
				&& barrel.getFluid().getFluid() == FluidRegistry.LAVA
                && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity();
    }

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		if (item.getItem() == Item.REGISTRY.getObject(dust)
				&& item.getMetadata() == 2
		    	&& barrel.getFluid().getFluid() == FluidRegistry.LAVA
		    	&& barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()) {
			
			barrel.setState(BarrelStates.OUTPUT);
			barrel.setContents(new ItemStack(Block.REGISTRY.getObject(skyStone), 1));

			barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 4.5f);
			
			return true;
		}
		
		return false;
	}
}
