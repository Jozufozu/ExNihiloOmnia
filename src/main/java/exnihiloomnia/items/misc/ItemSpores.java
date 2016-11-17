package exnihiloomnia.items.misc;

import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpores extends Item {

	public ItemSpores() {
		super();

		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.worldObj.isRemote) {
			BlockPos up = pos.add(0, hitY, 0);
			if (playerIn.canPlayerEdit(pos, facing, stack) && stack.stackSize != 0 && worldIn.getBlockState(pos) == Blocks.DIRT.getDefaultState()) {
				worldIn.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
				worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 0.3f, 1.5f);

				InventoryHelper.consumeItem(playerIn, stack);

				return EnumActionResult.SUCCESS;
			}
			else if (hitY == 1 && playerIn.canPlayerEdit(up, facing, stack) && worldIn.getBlockState(up).getBlock().isReplaceable(worldIn, pos) && canMushroomExist(worldIn, pos)) {
				if (worldIn.rand.nextInt(2) == 0)
					worldIn.setBlockState(up, Blocks.BROWN_MUSHROOM.getDefaultState());
				else
					worldIn.setBlockState(up, Blocks.RED_MUSHROOM.getDefaultState());

				InventoryHelper.consumeItem(playerIn, stack);

				worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.3f, 1.5f);

				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if (!entity.worldObj.isRemote && entity instanceof EntityCow) {
			entity.setDead();
			EntityMooshroom mooshroom = new EntityMooshroom(entity.worldObj);
			mooshroom.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			mooshroom.setHealth(entity.getHealth());
			mooshroom.renderYawOffset = entity.renderYawOffset;
			entity.worldObj.spawnEntityInWorld(mooshroom);
			entity.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY + (double)(entity.height / 2.0F), entity.posZ, 0.0D, 0.0D, 0.0D);

			InventoryHelper.consumeItem(player, item);
			
			return true;
		}
		
		return false;
	}

	private boolean canMushroomExist(World worldIn, BlockPos pos) {

		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate = worldIn.getBlockState(pos.down());
			return  iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, Blocks.RED_MUSHROOM) && (iblockstate.getBlock() == Blocks.MYCELIUM || (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL || worldIn.getLight(pos) < 13));
		}
		else
			return false;
	}
}
