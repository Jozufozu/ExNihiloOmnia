package exnihiloomnia.blocks.sieves;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.meshs.ISieveMesh;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSieve extends Block implements ITileEntityProvider {

	public BlockSieve(Material material)  {
		super(material);
		
		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(1.0f);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0.0D, 0, 1, (double) 12/16, 1);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}

		TileEntitySieve sieve = (TileEntitySieve) world.getTileEntity(pos);

		if (sieve != null) {
			if (sieve.canWork() && !sieve.hasSifter()) {
				//if (item != null && item.getItem() == Items.PRISMARINE_SHARD)
				//	sieve.setWorkSpeed(60);
				//else
				//	sieve.setWorkSpeed(30);
				sieve.doWork();
				world.playSound(null, pos, SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 0.3f, 0.6f);
			}
			else {
				if (sieve.hasMesh()) {
					if (player.isSneaking() && !ENOConfig.classic_sieve) {
						if (!world.isRemote) {
							EntityItem entity = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 1.2f, pos.getZ() + 0.5f, sieve.getMesh());
							world.spawnEntityInWorld(entity);
						}
						
						sieve.setMesh(null);
					}
					else if (item != null) {
						Block block = Block.getBlockFromItem(item.getItem());

						if (block != null) {
							if (sieve.getContents() == null && SieveRegistry.isSiftable(block.getStateFromMeta(item.getMetadata()))) {
								ItemStack contents = item.copy();
								contents.stackSize = 1;

								world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);

								sieve.setContents(contents);
								InventoryHelper.consumeItem(player, item);
							}
						}
					}
				}
				else {
					if (item != null && item.getItem() instanceof ISieveMesh) {
						ItemStack mesh = item.copy();
						mesh.stackSize = 1;
						
						sieve.setMesh(mesh);
						player.setHeldItem(hand, null);
					}
				}
			}
		}

		//Return true to keep buckets from pouring all over the damn place.
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)  {
		TileEntitySieve sieve = new TileEntitySieve();
		if (ENOConfig.classic_sieve)
			sieve.setMesh(new ItemStack(ENOItems.MESH_SILK_WHITE.setMaxDamage(Integer.MAX_VALUE - 1)));
		return sieve;
	}
}
