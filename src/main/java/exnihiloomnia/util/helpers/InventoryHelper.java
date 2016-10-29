package exnihiloomnia.util.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryHelper {
	
	public static ItemStack getContainer(ItemStack full) {
		ItemStack empty;
		
		if (full.getItem().hasContainerItem(full)) {
			empty = full.getItem().getContainerItem(full);
		} 
		else {
			empty = FluidContainerRegistry.drainFluidContainer(full);
		}
		
		return empty;
	}
	
	public static void giveItemStackToPlayer(EntityPlayer player, ItemStack item)
	{
		if(!player.worldObj.isRemote) {

			if (!player.inventory.addItemStackToInventory(item)) {
                player.dropItem(item, false);
			}
		}	
	}


	public static void consumeItem(@Nullable EntityPlayer player, @Nonnull ItemStack item) {
		if (player == null || !player.capabilities.isCreativeMode) {
			--item.stackSize;
			if (item.stackSize < 0)
				item.stackSize = 0;
		}
	}

	/*
	public static void consumeItem(@Nonnull EntityPlayer player, ItemStack item) {

		if (player.worldObj.isRemote) {
			if (player != null && !player.capabilities.isCreativeMode) {
				if (item.stackSize > 1)
					item.stackSize--;
				else {
					if (item.equals(player.getActiveItemStack()))
						player.setHeldItem(player.getActiveHand(), null);
					else {

						for (ItemStack stack : player.inventoryContainer.getInventory()) {
							if (ItemStack.areItemsEqual(stack, item) && stack.stackSize == 1)
								stack = null;
						}
					}
				}
			}
		}
	}
	*/
}
