package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.common.blocks.BlockBase;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBarrel extends BlockBase //implements ITileEntityProvider
{
    public static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(1.0 / 16.0, 0, 1.0 / 16.0, 15.0 / 16.0, 1.0, 15.0 / 16.0);
    
    public BlockBarrel(ResourceLocation registryName, Material materialIn)
    {
        this(registryName, materialIn, SoundType.STONE);
    }
    
    public BlockBarrel(ResourceLocation registryName, Material materialIn, SoundType soundType)
    {
        super(registryName, materialIn, soundType);
        
        this.setCreativeTab(ExNihiloTabs.BARRELS);
        this.fullBlock = false;
        this.setLightOpacity(1);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BARREL_AABB;
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
