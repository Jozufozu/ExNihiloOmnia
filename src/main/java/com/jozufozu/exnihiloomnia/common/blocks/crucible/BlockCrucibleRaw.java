package com.jozufozu.exnihiloomnia.common.blocks.crucible;

import com.jozufozu.exnihiloomnia.common.blocks.BlockBase;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCrucibleRaw extends BlockBase
{
    public BlockCrucibleRaw()
    {
        this(LibBlocks.RAW_CRUCIBLE, Material.GROUND, SoundType.GROUND);
        this.setHardness(1.0f);
    }
    
    public BlockCrucibleRaw(ResourceLocation registryName, Material materialIn, SoundType soundType)
    {
        super(registryName, materialIn, soundType);
        this.setLightOpacity(1);
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ == EnumFacing.UP ? BlockFaceShape.BOWL :BlockFaceShape.UNDEFINED;
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
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
    
    @Override
    public boolean canEntitySpawn(IBlockState state, Entity entityIn)
    {
        return false;
    }
}
