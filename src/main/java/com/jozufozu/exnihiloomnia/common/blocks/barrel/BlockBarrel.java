package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.client.renderers.TileEntityBarrelRenderer;
import com.jozufozu.exnihiloomnia.common.blocks.BlockBase;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockBarrel extends BlockBase implements ITileEntityProvider
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityBarrel barrel = (TileEntityBarrel) worldIn.getTileEntity(pos);
    
        if (barrel != null && !worldIn.isRemote)
        {
            ItemStack held = playerIn.getHeldItem(hand);
            
            if (barrel.getState().canExtractItems(barrel) && playerIn.isSneaking() && held.isEmpty())
            {
                IItemHandler barrelItems = barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                
                if (barrelItems != null)
                {
                    ItemHandlerHelper.giveItemToPlayer(playerIn, barrelItems.extractItem(0, 64, false));
                    worldIn.playSound(null, pos,
                            SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
            else
            {
                ItemStack in = ItemHandlerHelper.copyStackWithSize(held, 1);
                FluidStack fluidBefore = FluidUtil.getFluidContained(playerIn.getHeldItem(hand));
    
                if (!FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing))
                {
                    in = ItemHandlerHelper.insertItem(barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing), in, false);
                }
                else //TODO: Sounds don't play when the play is holding a stack of buckets
                {
                    FluidStack fluidContained = FluidUtil.getFluidContained(playerIn.getHeldItem(hand));
        
                    if (fluidContained != null)
                    {
                        Fluid fluid = fluidContained.getFluid();
                        worldIn.playSound(null, pos, fluid.getFillSound(fluidContained), SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    }
                    else if (fluidBefore != null)
                    {
                        Fluid fluid = fluidBefore.getFluid();
                        worldIn.playSound(null, pos, fluid.getEmptySound(fluidBefore), SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    }
                }
    
                if (in.isEmpty())
                {
                    Block blockFromItem = Block.getBlockFromItem(held.getItem());
        
                    if (blockFromItem != Blocks.AIR)
                    {
                        SoundType soundType = blockFromItem.getSoundType(blockFromItem.getStateFromMeta(held.getMetadata()), worldIn, pos, playerIn);
                        worldIn.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f);
                    }
                    
                    if (!playerIn.isCreative())
                    {
                        held.shrink(1);
                    }
                }
            }
        }
        return true;
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
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBarrel();
    }
    
    @Override
    public void registerModels()
    {
        super.registerModels();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer());
    }
}
