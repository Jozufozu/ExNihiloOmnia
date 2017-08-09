package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBarrelColored extends BlockBarrel
{
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    
    public BlockBarrelColored(ResourceLocation registryName, Material materialIn)
    {
        this(registryName, materialIn, SoundType.STONE);
    }
    
    public BlockBarrelColored(ResourceLocation registryName, Material materialIn, SoundType soundType)
    {
        super(registryName, materialIn, soundType);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(state));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < EnumDyeColor.values().length; i++)
        {
            items.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, COLOR);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(COLOR).getMetadata();
    }
    
    @Override
    public ItemBlock getItemBlock()
    {
        if (ExNihiloItems.hasRegisteredItems())
            return (ItemBlock) Item.getItemFromBlock(this);
        
        ItemBlock out = new ItemMultiTexture(this, this, item -> EnumDyeColor.byMetadata(item.getMetadata()).getDyeColorName());
        out.setRegistryName(getRegistryName());
        return out;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        ItemBlock itemBlock = getItemBlock();
        for (EnumDyeColor enumType : EnumDyeColor.values())
        {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.getMetadata(), new ModelResourceLocation(this.getRegistryName() + "_" + enumType.getName(), "inventory"));
        }
        
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve.class, new TileEntitySieveRenderer());
    }
}
