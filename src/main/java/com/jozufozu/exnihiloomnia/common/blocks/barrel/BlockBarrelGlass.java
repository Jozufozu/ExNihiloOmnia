package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class BlockBarrelGlass extends BlockBarrel
{
    public BlockBarrelGlass()
    {
        super(LibBlocks.GLASS_BARREL, Material.GLASS, SoundType.GLASS);
        this.setHardness(0.7f);
    }
    
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
