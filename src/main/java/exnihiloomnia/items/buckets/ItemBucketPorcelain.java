package exnihiloomnia.items.buckets;

import javax.annotation.Nullable;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.PorcelainBucketWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public class ItemBucketPorcelain extends ItemFluidContainer implements IFluidContainerItem{
	private final Block isFull;

	public ItemBucketPorcelain(Block block) {
		super(1000);

		this.setMaxStackSize(1);
		this.isFull = block;
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		boolean empty = this.isFull == Blocks.AIR;
		boolean doAction = !playerIn.isCreative();
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, empty);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, raytraceresult);
		
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}
		else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}
		else {
			BlockPos blockpos = raytraceresult.getBlockPos();

			if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
			}
			else if (empty) {
				if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemStackIn)) {
					return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
				}
				else {
					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					Material material = iblockstate.getMaterial();


					if (worldIn.getBlockState(blockpos).getBlock().equals(ENOBlocks.WITCHWATER) && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						fill(itemStackIn, new FluidStack(ENOFluids.WITCHWATER, getCapacity()), doAction);

						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
					}
					else if (material == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						fill(itemStackIn, new FluidStack(FluidRegistry.WATER, getCapacity()), doAction);

						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
					}
					else if (material == Material.LAVA && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						fill(itemStackIn, new FluidStack(FluidRegistry.LAVA, getCapacity()), doAction);

						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
					}
					else {
						return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
					}
				}
			}
			else {
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

				if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemStackIn)) {
					return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
				}
				else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)) {
					drain(itemStackIn, getCapacity(), doAction);

					playerIn.addStat(StatList.getObjectUseStats(this));
                    
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
				}
				else {
					return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
				}
			}
		}
	}
	
    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer worldIn, World pos, BlockPos posIn) {
        if (this.isFull == Blocks.AIR) {
            return false;
        }
        else {
            IBlockState iblockstate = pos.getBlockState(posIn);
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(pos, posIn);

            if (!pos.isAirBlock(posIn) && !flag && !flag1) {
                return false;
            }
            else {
                if (pos.provider.doesWaterVaporize() && this.isFull == Blocks.FLOWING_WATER) {
                    int x = posIn.getX();
                    int y = posIn.getY();
                    int z = posIn.getZ();
                    pos.playSound(worldIn, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (pos.rand.nextFloat() - pos.rand.nextFloat()) * 0.8F);

                    for (int i = 0; i < 8; ++i) {
                        pos.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
                else {
                    if (!pos.isRemote && (flag || flag1) && !material.isLiquid()) {
                        pos.destroyBlock(posIn, true);
                    }

                    SoundEvent soundevent = this.isFull == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
                    pos.playSound(worldIn, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    pos.setBlockState(posIn, this.isFull.getDefaultState(), 11);
                }

                return true;
            }
        }
    }

	@Override
	public FluidStack getFluid(ItemStack container) {
		Item bucket = container.getItem();

		if (bucket == ENOItems.BUCKET_PORCELAIN_LAVA)
			return new FluidStack(FluidRegistry.LAVA, getCapacity());

		if (bucket == ENOItems.BUCKET_PORCELAIN_WATER)
			return new FluidStack(FluidRegistry.WATER, getCapacity());

		if (bucket == ENOItems.BUCKET_PORCELAIN_WITCHWATER)
			return new FluidStack(ENOFluids.WITCHWATER, getCapacity());

		if (bucket == ENOItems.BUCKET_PORCELAIN_MILK)
			return new FluidStack(FluidRegistry.getFluid("milk"), getCapacity());

		return null;
	}

	private static Item getBucketItem(Fluid fluid) {
		if (fluid == FluidRegistry.WATER) {
			return ENOItems.BUCKET_PORCELAIN_WATER;
		}
		else if (fluid == FluidRegistry.LAVA) {
			return ENOItems.BUCKET_PORCELAIN_LAVA;
		}
		else if (fluid == ENOFluids.WITCHWATER) {
			return ENOItems.BUCKET_PORCELAIN_WITCHWATER;
		}
		else if (fluid == FluidRegistry.getFluid("milk"))
			return ENOItems.BUCKET_PORCELAIN_MILK;

		return null;
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return getCapacity();
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		if (container.stackSize != 1)
			return 0;

		// can only fill exact capacity
		if (resource == null || resource.amount < getCapacity())
			return 0;

		// already contains fluid?
		if (getFluid(container) != null)
			return 0;

		if (doFill) {
			Item full = getBucketItem(resource.getFluid());

			if (full != null)
				container.setItem(full);
		}

		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		// has to be exactly 1, must be handled from the caller
		if (container.stackSize != 1)
			return null;

		// can only drain everything at once
		if (maxDrain < getCapacity(container))
			return null;

		FluidStack fluidStack = getFluid(container);
		if (doDrain && fluidStack != null) {
			container.setItem(ENOItems.BUCKET_PORCELAIN_EMPTY);
		}

		return fluidStack;
	}

	public int getCapacity()
	{
		return capacity;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new PorcelainBucketWrapper(stack);
	}
}
