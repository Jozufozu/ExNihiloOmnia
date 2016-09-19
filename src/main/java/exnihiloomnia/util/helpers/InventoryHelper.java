package exnihiloomnia.util.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

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
	
	public static void consumeItem(EntityPlayer player, ItemStack item) {
		if (player != null && !player.capabilities.isCreativeMode) {
            item.stackSize--;
		}
	}
}
