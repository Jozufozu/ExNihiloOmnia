package exnihiloomnia.blocks.barrels.architecture;

import javax.annotation.Nullable;

import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public abstract class BarrelLogic {

	/**
	 * Called when a barrel state is set
	 * @param barrel the fresh new barrel
	 * @return true to be THE thing to run, or something like that. I actually don't know
	 */
	public boolean onActivate(TileEntityBarrel barrel) {
		return false;
	}

	/**
	 * @param barrel the barrel being ticked
	 * @return true if something happened
	 */
	public boolean onUpdate(TileEntityBarrel barrel) {
		return false;
	}

	/**
	 * @param barrel the barrel being tested
	 * @param item the item being tested
	 * @return true if item can be used on barrel
	 */
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		return false;
	}

	/**
	 * @param player can be null if the item is inserted by pipes!
	 * @param hand for the special little snowflakes that are buckets
	 * @param barrel the barrel
	 * @param item the item to be checked
	 * @return true to have the default stack size decrease
	 */
	public boolean onUseItem(@Nullable EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		return false;
	}
}
