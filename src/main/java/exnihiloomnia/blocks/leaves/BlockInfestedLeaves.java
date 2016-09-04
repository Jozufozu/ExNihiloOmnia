package exnihiloomnia.blocks.leaves;


import exnihiloomnia.ENOConfig;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider {

    public BlockInfestedLeaves() {
        super();
        this.setCreativeTab(ENOItems.ENO_TAB);
        this.isBlockContainer = true;
        this.setLightOpacity(1);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{DECAYABLE, CHECK_DECAY}) {
        };
    }

    @Override
    public int getMetaFromState(IBlockState state) {return 0;}

    @Override
    public IBlockState getStateFromMeta(int meta) {return this.getDefaultState();}

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        //no saplings for you
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
    {
        TileEntityInfestedLeaves te = (TileEntityInfestedLeaves)world.getTileEntity(pos);

        if (te != null)
        {
            te.dying = true;
        }
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta)
    {
        return BlockPlanks.EnumType.byMetadata((meta & 3) % 4);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return java.util.Arrays.asList(new ItemStack(this));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (!world.isRemote)
        {
            TileEntityInfestedLeaves leaves = (TileEntityInfestedLeaves) world.getTileEntity(pos);

            if (leaves != null)
            {
                if (world.rand.nextFloat() < leaves.getProgress() * ENOConfig.string_chance) {
                    EntityItem entity = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + .5f, pos.getZ() + 0.5f, new ItemStack(Items.STRING));
                    world.spawnEntityInWorld(entity);
                }

                if (world.rand.nextFloat() < leaves.getProgress() * ENOConfig.string_chance / 4.0d) {
                    EntityItem entity = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + .5f, pos.getZ() + 0.5f, new ItemStack(Items.STRING));
                    world.spawnEntityInWorld(entity);
                }
            }
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityInfestedLeaves();
    }
}

