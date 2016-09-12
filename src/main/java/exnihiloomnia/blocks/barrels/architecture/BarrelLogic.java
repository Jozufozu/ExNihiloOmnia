package exnihiloomnia.blocks.barrels.architecture;

import javax.annotation.Nullable;

import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public abstract class BarrelLogic {
	public boolean onActivate(TileEntityBarrel barrel) {
		return false;
	}

	public boolean onUpdate(TileEntityBarrel barrel) {
		return false;
	}
	
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		return false;
	}

	//Player CAN be null if the item is inserted by pipes! //hand is for the special little snowflakes that are buckets
	public boolean onUseItem(@Nullable EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		return false;
	}

	protected void consumeItem(EntityPlayer player, ItemStack item) {
		if (player == null || !player.capabilities.isCreativeMode) {
			item.stackSize -= 1;
			
			if (item.stackSize == 0) {
				item = null;
			}
		}
	}
}
