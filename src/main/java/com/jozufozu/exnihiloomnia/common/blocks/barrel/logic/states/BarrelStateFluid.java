package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelLogic;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.FluidMixingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BarrelStateFluid extends BarrelState
{
    public BarrelStateFluid()
    {
        super(BarrelStates.ID_FLUID);
        this.logic.add(new FluidMixingTrigger());
    }
    
    @Override
    public boolean canInteractWithItems(TileEntityBarrel barrel)
    {
        return false;
    }
    
    @Override
    public void draw(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        super.draw(barrel, x, y, z, partialTicks);
        
        renderFluid(barrel, x, y, z, partialTicks);
    }
    
    public static class FluidMixingTrigger extends BarrelLogic
    {
        @Override
        public boolean onUpdate(TileEntityBarrel barrel)
        {
            World world = barrel.getWorld();
            if (!world.isRemote)
            {
                IBlockState blockState = world.getBlockState(barrel.getPos().up());
    
                Fluid fluid = null;
                
                Block block = blockState.getBlock();
                
                if (block instanceof IFluidBlock)
                {
                    fluid = ((IFluidBlock) block).getFluid();
                }
                else if (blockState.getMaterial() == Material.LAVA)
                {
                    fluid = FluidRegistry.LAVA;
                }
                else if (blockState.getMaterial() == Material.WATER)
                {
                    fluid = FluidRegistry.WATER;
                }
                
                if (fluid == null)
                {
                    return false;
                }
                
                FluidStack on = new FluidStack(fluid, 1000);
                FluidStack in = barrel.getFluid();
    
                if (in == null)
                {
                    return false;
                }
                
                FluidMixingRecipe recipe = RegistryManager.getMixing(in, on);
    
                if (recipe == null)
                {
                    return false;
                }
                
                barrel.setState(BarrelStates.ITEMS);
    
                world.playSound(null, barrel.getPos(), recipe.getCraftSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                barrel.setItem(recipe.getOutput());
                barrel.setFluid(null);
    
                return true;
            }
            return false;
        }
    }
}
