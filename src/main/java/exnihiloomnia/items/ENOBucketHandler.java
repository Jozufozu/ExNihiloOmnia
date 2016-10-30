package exnihiloomnia.items;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.fluids.ENOFluids;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ENOBucketHandler {

    public static void registerBuckets() {
        FluidContainerRegistry.registerFluidContainer(ENOFluids.WITCHWATER, new ItemStack(ENOItems.BUCKET_WITCHWATER), new ItemStack(Items.BUCKET));
        FluidContainerRegistry.registerFluidContainer(ENOFluids.WITCHWATER, new ItemStack(ENOItems.BUCKET_PORCELAIN_WITCHWATER), new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.LAVA, new ItemStack(ENOItems.BUCKET_PORCELAIN_LAVA), new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(ENOItems.BUCKET_PORCELAIN_WATER), new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY));
    }

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

    @SubscribeEvent
    public void bucketFill(FillBucketEvent event) {
        Block block = event.getEntityLiving().worldObj.getBlockState(event.getTarget().getBlockPos()).getBlock();

        if (event.getEmptyBucket().getItem() == Items.BUCKET && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            if (block == ENOBlocks.WITCHWATER) {
                event.setResult(Event.Result.ALLOW);
                event.setFilledBucket(new ItemStack(ENOItems.BUCKET_WITCHWATER));
                event.getEntityLiving().worldObj.setBlockToAir(event.getTarget().getBlockPos());
            }
        }
    }
}

