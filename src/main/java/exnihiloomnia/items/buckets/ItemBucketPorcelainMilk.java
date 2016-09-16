package exnihiloomnia.items.buckets;

import exnihiloomnia.items.ENOItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBucketPorcelainMilk extends ItemBucketMilk {
	
	public ItemBucketPorcelainMilk() {
		this.setContainerItem(ENOItems.BUCKET_PORCELAIN_EMPTY);
		this.setMaxStackSize(1);
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase playerIn) {
        if (playerIn instanceof EntityPlayer && !((EntityPlayer)playerIn).capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        if (!worldIn.isRemote) {
            playerIn.curePotionEffects(new ItemStack(Items.MILK_BUCKET, 1));
        }

        ((EntityPlayer) playerIn).addStat(StatList.getObjectUseStats(this));
        
        return stack.stackSize <= 0 ? new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY) : stack;
    }
}
