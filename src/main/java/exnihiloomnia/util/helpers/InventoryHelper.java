package exnihiloomnia.util.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

	public static void dropItemInWorld(World world, BlockPos pos, ItemStack itemStack) {
		if (itemStack != null)
			net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
	}

	public static void dropItemInWorld(World world, BlockPos pos, double yOffset, ItemStack itemStack) {
		if (itemStack != null)
			net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + yOffset, pos.getZ(), itemStack);
	}
	
	public static void giveItemStackToPlayer(EntityPlayer player, ItemStack item) {
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
}
