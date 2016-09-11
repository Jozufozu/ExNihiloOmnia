package exnihiloomnia.blocks.barrels.states.empty.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import net.minecraft.util.EnumHand;

public class CompostStateTrigger extends BarrelLogic{
	
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) 
	{
		return CompostRegistry.isCompostable(item);
	}
	
	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item)
	{
		CompostRegistryEntry recipe = CompostRegistry.getEntryForItemStack(item);
		
		if (recipe != null)
		{
			barrel.setState(BarrelStates.COMPOST);
			barrel.getState().useItem(player, hand, barrel, item);
			
			return true;
		}

		return false;
	}
}
