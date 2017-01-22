package exnihiloomnia.blocks.barrels;

import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBarrel extends Block implements ITileEntityProvider {
    protected static final AxisAlignedBB AABB_BARREL = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

	public BlockBarrel(Material material, SoundType sound) {
		super(material);

		this.setSoundType(sound);
		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(1.2f);
	}

	@Override
	public boolean canCreatureSpawn(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB_BARREL;
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null && tile instanceof TileEntityBarrel) {
			return ((TileEntityBarrel)tile).getLuminosity();
		}
		
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ) {
	    if (player == null)
			return false;

		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);

		if (barrel != null) {
			if (barrel.canExtractItem(0)) {
				if (!world.isRemote) {
					InventoryHelper.dropItemInWorld(world, pos, 1.2, barrel.getItemHandler().getStackInSlot(0));
				}
				
				barrel.setInventorySlotContents(0, null);
			}
			else if (item != null)
				barrel.getState().useItem(player, hand, barrel, item);
		}

		//Return true to keep buckets from pouring all over the damn place.
		return true;
	}

	@Override
	@Nonnull
	public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
		TileEntityBarrel barrel = new TileEntityBarrel();
		barrel.setState(BarrelStates.EMPTY);
		
		return barrel;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	@Nonnull
	public BlockRenderLayer getBlockLayer() {
		if (this.getBlockState().getBaseState().getMaterial().isOpaque())
			return BlockRenderLayer.SOLID;
		else
			return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);

		if (barrel != null) {

			if (world.getGameRules().getBoolean("doTileDrops")) {
				for (ItemStack i : barrel.getOutput()) {
					InventoryHelper.dropItemInWorld(world, pos, i);
				}

				InventoryHelper.dropItemInWorld(world, pos, barrel.getContents());
			}
			barrel.setLuminosity(0);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, @Nonnull BlockPos pos) {
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
	public boolean isBlockSolid(IBlockAccess worldIn, @Nonnull BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
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
