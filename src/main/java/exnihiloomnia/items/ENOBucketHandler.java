package exnihiloomnia.items;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ENOBucketHandler {

	@SubscribeEvent
    public void onEntityInteraction(PlayerInteractEvent.EntityInteract event) {
		if (event.getEntityPlayer() == null || event.getTarget() == null || !(event.getTarget() instanceof EntityCow))
            return;

        ItemStack equipped = event.getEntityPlayer().getActiveItemStack();
        if (equipped == null || equipped.getItem() != ENOItems.BUCKET_PORCELAIN_EMPTY)
            return;

        EntityPlayer player = event.getEntityPlayer();

        if (!player.capabilities.isCreativeMode) {
        	if (--equipped.stackSize == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(ENOItems.BUCKET_PORCELAIN_MILK));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(ENOItems.BUCKET_PORCELAIN_MILK))) {
                player.dropItem(new ItemStack(ENOItems.BUCKET_PORCELAIN_MILK, 1), false);
            }
        }
    }
}

