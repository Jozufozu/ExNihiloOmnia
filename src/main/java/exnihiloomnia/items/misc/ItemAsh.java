package exnihiloomnia.items.misc;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAsh extends Item {
	
	public ItemAsh() {
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() instanceof IGrowable) {
			IGrowable igrowable = (IGrowable)state.getBlock();

			if (igrowable.canGrow(worldIn, pos, state, worldIn.isRemote)) {
				if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, state)) {
					if (!worldIn.isRemote) {
						worldIn.playEvent(2005, pos, 0);
						
						if (worldIn.rand.nextInt(5) == 0) {
							igrowable.grow(worldIn, worldIn.rand, pos, state);
						}
					}
				}
				
				--stack.stackSize;
				return EnumActionResult.SUCCESS;
			}
		}

        return EnumActionResult.PASS;
    }
}
