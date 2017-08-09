package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBarrelWood extends BlockBarrel implements IModelRegister
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = BlockPlanks.VARIANT;
    
    public BlockBarrelWood()
    {
        super(LibBlocks.WOODEN_BARREL, Material.WOOD, SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));
        this.setHardness(2.0f);
        this.setResistance(3.0f);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(state));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < BlockPlanks.EnumType.values().length; i++)
        {
            items.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }
    
    @Override
    public ItemBlock getItemBlock()
    {
        if (ExNihiloItems.hasRegisteredItems())
            return (ItemBlock) Item.getItemFromBlock(this);
        
        ItemBlock out = new ItemMultiTexture(this, this, item -> BlockPlanks.EnumType.byMetadata(item.getMetadata()).getUnlocalizedName());
        out.setRegistryName(getRegistryName());
        return out;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        ItemBlock itemBlock = getItemBlock();
        for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values())
        {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.getMetadata(), new ModelResourceLocation(ExNihilo.MODID + ":barrel_wood_" + enumType.getUnlocalizedName(), "inventory"));
        }
        
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve.class, new TileEntitySieveRenderer());
    }
}
