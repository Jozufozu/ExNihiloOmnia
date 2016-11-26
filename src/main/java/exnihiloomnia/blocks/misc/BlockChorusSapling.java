package exnihiloomnia.blocks.misc;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockChorusSapling extends BlockBush implements IGrowable {

    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 3);
    public static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockChorusSapling() {
        super(Material.PLANTS, MapColor.MAGENTA);
        setSoundType(SoundType.PLANT);
        setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 3));
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ENOItems.CHORUS_SEEDS;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SAPLING_AABB;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.END_STONE;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()) == Blocks.END_STONE.getDefaultState();
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {

            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
                this.grow(worldIn, pos, state, rand);
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(STAGE) < 3)
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        else
            this.generateTree(worldIn, pos, rand);
    }

    public void generateTree(World worldIn, BlockPos pos, Random rand) {
        BlockChorusFlower.generatePlant(worldIn, pos, rand, 8);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState().withProperty(STAGE, 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }
}
