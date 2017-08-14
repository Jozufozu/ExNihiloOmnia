package com.jozufozu.exnihiloomnia.common.blocks.crucible;

import com.jozufozu.exnihiloomnia.client.renderers.TileEntityCrucibleRenderer;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockCrucible extends BlockCrucibleRaw implements ITileEntityProvider
{
    public BlockCrucible()
    {
        super(LibBlocks.CRUCIBLE, Material.ROCK, SoundType.STONE);
        this.setLightOpacity(1);
        this.setHardness(3.0f);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityCrucible crucible = (TileEntityCrucible) worldIn.getTileEntity(pos);
        
        if (crucible != null && !worldIn.isRemote)
        {
            ItemStack held = playerIn.getHeldItem(hand);
            
            ItemStack in = ItemHandlerHelper.copyStackWithSize(held, 1);
    
            if (!FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing))
            {
                in = ItemHandlerHelper.insertItem(crucible.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing), in, false);
            }
            else
            {
                FluidStack fluidContained = FluidUtil.getFluidContained(playerIn.getHeldItem(hand));
    
                if (fluidContained != null)
                {
                    Fluid fluid = fluidContained.getFluid();
                    worldIn.playSound(null, pos, fluid.getFillSound(fluidContained), SoundCategory.NEUTRAL, 1.0f, 1.0f);
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
                held.shrink(1);
            }
        }
        
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCrucible();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        super.registerModels();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
    }
}
