package exnihiloomnia.blocks.barrels.states.compost.logic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.util.EnumHand;

public class PodzolStateTrigger extends BarrelLogic{
	
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) 
	{
		if (barrel.getTimerStatus() == -1.0d)
		{
			Block block = Block.getBlockFromItem(item.getItem());
			
			if (block != null && block.getBlockState().getBaseState().getMaterial() == Material.LEAVES)
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item)
	{
		if (barrel.getTimerStatus() == -1.0d)
		{
			Block block = Block.getBlockFromItem(item.getItem());
			
			if (block != null && block.getBlockState().getBaseState().getMaterial() == Material.LEAVES)
				barrel.setState(BarrelStates.PODZOL);
		}
		
		return false;
	}
}
