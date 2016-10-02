package exnihiloomnia.blocks.barrels;

import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBarrel extends Block implements ITileEntityProvider {
    protected static final AxisAlignedBB AABB_BARREL = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

	public BlockBarrel(Material material, SoundType sound) {
		super(material);

		this.setSoundType(sound);
		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(1.2f);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB_BARREL;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null && tile instanceof TileEntityBarrel) {
			return ((TileEntityBarrel)tile).getLuminosity();
		}
		
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (player == null) {
			return false;
		}

		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);

		if (barrel != null) {
			if (item != null) {
				//if (barrel.getState().canManipulateFluids(barrel))
				//	FluidUtil.interactWithFluidHandler(item, barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), player);
				barrel.getState().useItem(player, hand, barrel, item);
			}
			if (barrel.canExtractItem(0, barrel.getItemHandler().getStackInSlot(0), null)) {
				if (!world.isRemote) {
					EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f, barrel.getItemHandler().getStackInSlot(0));

					double f3 = 0.05F;
					entityitem.motionX = world.rand.nextGaussian() * f3;
					entityitem.motionY = (0.2d);
					entityitem.motionZ = world.rand.nextGaussian() * f3;
					entityitem.setDefaultPickupDelay();

					world.spawnEntityInWorld(entityitem);
				}
				
				barrel.setInventorySlotContents(0, null);
			}
		}

		//Return true to keep buckets from pouring all over the damn place.
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		TileEntityBarrel barrel = new TileEntityBarrel();
		barrel.setState(BarrelStates.EMPTY);
		
		return barrel;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		if (this.getBlockState().getBaseState().getMaterial().isOpaque()) {
			return BlockRenderLayer.SOLID;
		}
		else {
			return BlockRenderLayer.CUTOUT_MIPPED;
		}
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);
		barrel.setLuminosity(0);
		
		return super.removedByPlayer(state ,world, pos, player, willHarvest);
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);
		barrel.setLuminosity(0);
		
		super.onBlockExploded(world, pos, explosion);
    }
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		//Barrels should never break with the wrong tool.
		return true;
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public boolean isVisuallyOpaque() {
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
