package exnihiloomnia.blocks.crucibles;

import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockCrucible extends Block implements ITileEntityProvider {

	public BlockCrucible()
	{
		super(Material.CLAY);

		this.setSoundType(SoundType.STONE);
		this.setHardness(1.0f);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(pos);

		if (item != null && crucible != null)
		{
			if (FluidContainerRegistry.isEmptyContainer(item) )
			{
				ItemStack full = FluidContainerRegistry.fillFluidContainer(crucible.getFluid(), item);

				if (full != null)
				{
					if (player != null) {
						if (!player.capabilities.isCreativeMode) {
							if (item.stackSize > 1) {
								item.stackSize--;
								InventoryHelper.giveItemStackToPlayer(player, full);
							} else {
								player.setHeldItem(hand, full);
							}
						}

						crucible.getTank().drain(1000, true);
						return true;
					}
				}
			}

			ItemStack contents = item.copy();
			contents.stackSize = 1;

			if (crucible.canInsertItem(contents))
			{
				crucible.addSolid(CrucibleRegistry.getItem(contents).getSolidVolume());
                crucible.sync();

				world.playSound(null, pos, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
				InventoryHelper.consumeItem(player, item);
				return true;
			}
		}

		//Return true to keep buckets from pouring all over the damn place.
		return true;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(pos);

		if (crucible != null) {
            if (crucible.getFluid() != null) {
                if (crucible.getFluidFullness() > crucible.getSolidFullness()) {

                    FluidStack fluid = crucible.getFluid();

					if (fluid != null && fluid.getFluid() != null) {
						return crucible.getFluid().getFluid().getLuminosity();
					}
				}
			}
            if (crucible.getLastItemAdded() != null) {
                if (crucible.getFluidFullness() < crucible.getSolidFullness()) {
                    ItemStack item = crucible.getLastItemAdded();
                    Block block = Block.getBlockFromItem(item.getItem());
                    return block.getLightValue(block.getStateFromMeta(item.getMetadata()));
                }
            }
		}
		
		return 0;
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
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityCrucible();
	}
}
