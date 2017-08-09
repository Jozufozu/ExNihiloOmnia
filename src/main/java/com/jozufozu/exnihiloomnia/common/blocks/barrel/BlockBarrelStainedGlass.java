package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;

public class BlockBarrelStainedGlass extends BlockBarrelColored
{
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    
    public BlockBarrelStainedGlass()
    {
        super(LibBlocks.STAINED_GLASS_BARREL, Material.GLASS, SoundType.GLASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setHardness(0.7f);
    }
    
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
