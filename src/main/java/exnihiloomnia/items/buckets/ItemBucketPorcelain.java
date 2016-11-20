package exnihiloomnia.items.buckets;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBucketPorcelain extends Item {
	private final Block containedBlock;

	public ItemBucketPorcelain(Block block) {

		this.setContainerItem(ENOItems.BUCKET_PORCELAIN_EMPTY);
		this.setMaxStackSize(1);
		this.containedBlock = block;
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		boolean flag = this.containedBlock == Blocks.AIR;
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);

		else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);

		else {
			BlockPos blockpos = raytraceresult.getBlockPos();

			if (!worldIn.isBlockModifiable(playerIn, blockpos))
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
			else if (flag) {
				if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemStackIn))
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);

				else {
					IBlockState iblockstate = worldIn.getBlockState(blockpos);

					if (iblockstate.getBlock() == Blocks.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {

						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemStackIn, playerIn, ENOItems.BUCKET_PORCELAIN_WATER));
					}
					else if (iblockstate.getBlock() == Blocks.LAVA  && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {

						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemStackIn, playerIn, ENOItems.BUCKET_PORCELAIN_LAVA));
					}
					else if (iblockstate.getBlock() == ENOBlocks.WITCHWATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {

						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemStackIn, playerIn, ENOItems.BUCKET_PORCELAIN_WITCHWATER));
					}
					else
						return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
				}
			}
			else {
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

				if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemStackIn))
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);

				else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)) {
					playerIn.addStat(StatList.getObjectUseStats(this));
					return !playerIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY)) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
				}
				else
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
			}
		}
	}

	private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket)
	{
		if (player.capabilities.isCreativeMode)
			return emptyBuckets;

		else if (--emptyBuckets.stackSize <= 0)
			return new ItemStack(fullBucket);

		else {
			if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
				player.dropItem(new ItemStack(fullBucket), false);

			return emptyBuckets;
		}
	}

	public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn) {
		if (this.containedBlock == Blocks.AIR)
			return false;
		else {
			IBlockState iblockstate = worldIn.getBlockState(posIn);
			Material material = iblockstate.getMaterial();
			boolean flag = !material.isSolid();
			boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

			if (!worldIn.isAirBlock(posIn) && !flag && !flag1)
				return false;
			else {
				if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER) {
					int l = posIn.getX();
					int i = posIn.getY();
					int j = posIn.getZ();
					worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

					for (int k = 0; k < 8; ++k)
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
				}
				else {
					if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
						worldIn.destroyBlock(posIn, true);

					SoundEvent soundevent = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
					worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
					worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
				}

				return true;
			}
		}
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, net.minecraft.nbt.NBTTagCompound nbt) {
		return new UniversalPorcelainBucketWrapper(stack);
	}
}
