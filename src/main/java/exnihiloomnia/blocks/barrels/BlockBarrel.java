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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public class BlockBarrel extends Block implements ITileEntityProvider
{
    protected static final AxisAlignedBB AABB_BARREL = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

	public BlockBarrel(Material material, SoundType sound) {
		super(material);

		this.setSoundType(sound);
		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(1.2f);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB_BARREL;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);

		if (barrel != null)
		{
			return barrel.getLuminosity();
		}
		
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ) {

	    if (player == null)
		{
			return false;
		}

		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);

		if (barrel != null)
		{
			if (item != null) {
				if (barrel.getState().canUseItem(barrel, item)) {
					barrel.getState().useItem(player, barrel, item);
				}
			}
			else if (barrel.canExtractItem(0, barrel.getStackInSlot(0), EnumFacing.DOWN))
			{
				if(!world.isRemote)
				{
					EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f, barrel.getStackInSlot(0));

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
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		TileEntityBarrel barrel = new TileEntityBarrel();
		barrel.setState(BarrelStates.EMPTY);
		
		return barrel;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{ 
		return false; 
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		if (this.getBlockState().getBaseState().getMaterial().isOpaque())
		{
			return BlockRenderLayer.SOLID;
		}
		else
		{
			return BlockRenderLayer.TRANSLUCENT;
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);
		barrel.setLuminosity(0);
		
		return super.removedByPlayer(state ,world, pos, player, willHarvest);
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);
		barrel.setLuminosity(0);
		
		super.onBlockExploded(world, pos, explosion);
    }
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		//Barrels should never break with the wrong tool.
		return true;
	}
}
