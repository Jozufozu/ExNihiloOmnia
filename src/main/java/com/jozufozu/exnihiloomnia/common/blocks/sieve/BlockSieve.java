package com.jozufozu.exnihiloomnia.common.blocks.sieve;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.client.renderers.TileEntitySieveRenderer;
import com.jozufozu.exnihiloomnia.common.blocks.BlockBase;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockSieve extends BlockBase implements ITileEntityProvider, IModelRegister
{
    public static final PropertyEnum<EnumType> VARIANT = BlockPlanks.VARIANT;
    
    private static final AxisAlignedBB SIEVE_AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 13.0d / 16.0d, 1.0d);
    
    public BlockSieve()
    {
        super(LibBlocks.SIEVE, Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.OAK));
        this.fullBlock = false;
        this.setLightOpacity(1);
        this.setHardness(2.0f);
        this.setResistance(3.0f);
        this.setSoundType(SoundType.WOOD);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntitySieve sieve = (TileEntitySieve) worldIn.getTileEntity(pos);
        
        if (sieve != null)
        {
            ItemStack held = playerIn.getHeldItem(hand);
            
            if (sieve.hasContents() && sieve.hasMesh())
            {
                sieve.queueWork(playerIn, held);
    
                SoundType soundType = sieve.blockSound(playerIn);
                worldIn.playSound(null, pos, soundType.getHitSound(), SoundCategory.BLOCKS, 0.2f, soundType.getPitch() * 0.8f + worldIn.rand.nextFloat() * 0.4f);
            }
            else if (!worldIn.isRemote)
            {
                if (!sieve.hasMesh())
                {
                    sieve.trySetMesh(playerIn, held);
                }
                else
                {
                    if (playerIn.isSneaking() && held == ItemStack.EMPTY)
                    {
                        sieve.removeMesh(playerIn);
                        worldIn.playSound(null, pos,
                                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        return true;
                    }
                    
                    if (RegistryManager.siftable(held))
                    {
                        sieve.insertContents(playerIn, held);
                    }
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntitySieve sieve = (TileEntitySieve) worldIn.getTileEntity(pos);
    
        if (sieve != null)
        {
            spawnAsEntity(worldIn, pos, sieve.getMesh());
        }
        
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(state));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SIEVE_AABB;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
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

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < EnumType.values().length; i++)
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
        return this.blockState.getBaseState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySieve();
    }
    
    @Override
    public ItemBlock getItemBlock()
    {
        if (ExNihiloItems.hasRegisteredItems())
            return (ItemBlock) Item.getItemFromBlock(this);
        
        ItemBlock out = new ItemMultiTexture(this, this, item -> EnumType.byMetadata(item.getMetadata()).getUnlocalizedName());
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
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.getMetadata(), new ModelResourceLocation(ExNihilo.MODID + ":sieve_" + enumType.getUnlocalizedName(), "inventory"));
        }
    
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve.class, new TileEntitySieveRenderer());
    }
}
