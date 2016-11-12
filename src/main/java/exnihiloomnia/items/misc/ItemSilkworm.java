package exnihiloomnia.items.misc;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.leaves.TileEntityInfestedLeaves;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSilkworm extends Item {
	
    public ItemSilkworm() {
        super();
        this.setCreativeTab(ENOItems.ENO_TAB);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock().isLeaves(state, worldIn, pos) && !state.getBlock().equals(ENOBlocks.INFESTED_LEAVES)) {

            worldIn.setBlockState(pos, ENOBlocks.INFESTED_LEAVES.getDefaultState(), 2);
            TileEntityInfestedLeaves leaves = (TileEntityInfestedLeaves) worldIn.getTileEntity(pos);
            leaves.setMimicBlock(state);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 0.5f, 1.0f);

            InventoryHelper.consumeItem(playerIn, stack);
            return EnumActionResult.SUCCESS;
        }
        
        return EnumActionResult.PASS;
    }
}
