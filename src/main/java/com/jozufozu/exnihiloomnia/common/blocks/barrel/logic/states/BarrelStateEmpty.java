package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class BarrelStateEmpty extends BarrelState
{
    public BarrelStateEmpty()
    {
        super(BarrelStates.ID_EMPTY);
        this.logic.add(new FluidFillTrigger());
        this.logic.add(new CompostTrigger());
    }
    
    @Override
    public void activate(TileEntityBarrel barrel, @Nullable BarrelState previousState)
    {
        barrel.setItem(ItemStack.EMPTY);
        barrel.setFluid(null);
        barrel.color = null;
        barrel.compostAmount = 0;
        
        super.activate(barrel, previousState);
    }
    
    public static class CompostTrigger extends BarrelLogic
    {
        @Override
        public boolean canUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
        {
            return RegistryManager.getCompost(itemStack) != null;
        }
    
        @Override
        public EnumInteractResult onUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
        {
            CompostRecipe compost = RegistryManager.getCompost(itemStack);
    
            if (compost == null)
            {
                return EnumInteractResult.PASS;
            }
            
            if (!barrel.getWorld().isRemote)
            {
                barrel.setState(BarrelStates.COMPOST_COLLECT);
                
                barrel.setItem(compost.getOutput());
                
                barrel.color = compost.getColor();
                barrel.compostAmount = compost.getAmount();
            }
            return EnumInteractResult.CONSUME;
        }
    }
    
    public static class FluidFillTrigger extends BarrelLogic
    {
        @Override
        public boolean canFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
        {
            return true;
        }
    
        @Override
        public EnumInteractResult onFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
        {
            if (!barrel.getWorld().isRemote)
            {
                barrel.setState(BarrelStates.FLUID);
            }
            return EnumInteractResult.INSERT;
        }
    }
}
