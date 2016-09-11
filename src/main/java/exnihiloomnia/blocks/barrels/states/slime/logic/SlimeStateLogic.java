package exnihiloomnia.blocks.barrels.states.slime.logic;

import exnihiloomnia.items.crooks.ItemCrook;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SlimeStateLogic extends BarrelLogic{
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() == -1)
		{
			barrel.startTimer(1000);
		}
		return false;
	}

    @Override
    public boolean canUseItem(TileEntityBarrel barrel, ItemStack item)
    {
        return item.getItem() instanceof ItemCrook;
    }

	@Override
	public boolean onUseItem(@Nullable EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		if (barrel.getTimerStatus() >= 1.0d && barrel.getWorld().isAirBlock(barrel.getPos().up()))
		{
		    barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_SLIME_PLACE, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            if (player != null){
                if (!player.isCreative())
                    item.attemptDamageItem(1, barrel.getWorld().rand);
            }
            else
                item.attemptDamageItem(1, barrel.getWorld().rand);
			if (!barrel.getWorld().isRemote)
			{
				BlockPos pos = barrel.getPos();
				EntitySlime slime = new EntitySlime(barrel.getWorld());
				slime.setPositionAndRotation(pos.getX() + .5f, pos.getY() + 1f, pos.getZ() + .5f, 0, barrel.getWorld().rand.nextInt(360));
                slime.setHealth(1);

				barrel.getWorld().spawnEntityInWorld(slime);
			}

			barrel.setState(BarrelStates.EMPTY);

			return true;
		}
		return false;
	}
}
