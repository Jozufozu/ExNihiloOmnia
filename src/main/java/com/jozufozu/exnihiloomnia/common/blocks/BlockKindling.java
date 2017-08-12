package com.jozufozu.exnihiloomnia.common.blocks;

import com.jozufozu.exnihiloomnia.common.ModConfig;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockKindling extends BlockBase
{
    public static final AxisAlignedBB KINDLING_BB = new AxisAlignedBB(4.0 / 16.0, 0.0, 4.0 / 16.0, 12.0 / 16.0, 1.0 / 16.0, 12.0 / 16.0);
    
    public static final PropertyBool LIT = PropertyBool.create("lit");
    
    public BlockKindling()
    {
        super(LibBlocks.KINDLING, Material.WOOD, SoundType.WOOD);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LIT, true));
        this.setLightOpacity(0);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (state.getValue(LIT))
        {
            return true;
        }
        
        ItemStack use = playerIn.getHeldItem(hand);
        
        if (use.getItem() == Items.FLINT_AND_STEEL)
        {
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos, state.withProperty(LIT, true), 3);
                worldIn.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
                
                if (!playerIn.isCreative())
                {
                    use.attemptDamageItem(1, worldIn.rand, (EntityPlayerMP) playerIn);
                }
            }
            return true;
        }
        
        if (use.getItem() == Items.STICK)
        {
            if (!worldIn.isRemote)
            {
                if (worldIn.rand.nextDouble() <= ModConfig.blocks.kindlingLightChance)
                {
                    worldIn.setBlockState(pos, state.withProperty(LIT, true), 3);
    
                    if (!playerIn.isCreative())
                    {
                        use.shrink(1);
                    }
                }
                
                worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.BLOCKS, 0.4f, 1.0f);
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && state.getValue(LIT))
        {
            worldIn.setBlockToAir(pos);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.3f);

            for (int i = 0; i < 2 + rand.nextInt(3); i++)
            {
                spawnAsEntity(worldIn, pos, new ItemStack(ExNihiloItems.ASH));
            }
        }
    }
    
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (!stateIn.getValue(LIT))
        {
            return;
        }
        
        if (rand.nextInt(24) == 0)
        {
            worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            double d0 = (double)pos.getX() + rand.nextDouble() * 0.5 + 0.25;
            double d1 = (double)pos.getY() + rand.nextDouble() * 0.5 + 0.125;
            double d2 = (double)pos.getZ() + rand.nextDouble() * 0.5 + 0.25;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote && !canPlaceBlockAt(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        BlockPos down = pos.down();
        return worldIn.getBlockState(down).isSideSolid(worldIn, down, EnumFacing.UP);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getMetaFromState(state) * 15;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return KINDLING_BB;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIT);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.blockState.getBaseState().withProperty(LIT, meta == 1);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LIT) ? 1 : 0;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
    
    @Override
    public boolean canEntitySpawn(IBlockState state, Entity entityIn)
    {
        return false;
    }
}
