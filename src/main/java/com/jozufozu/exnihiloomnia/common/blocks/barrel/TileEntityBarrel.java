package com.jozufozu.exnihiloomnia.common.blocks.barrel;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntity implements ITickable
{
    public BarrelState state;
    
    private FluidTank tank = new FluidTank(1000);
    private ItemStackHandler itemHandler = new InputOutputItemHandler();
    
    @Override
    public void update()
    {
    
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&& state.canInteractWithItems(this))
            return (T) itemHandler;
        
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canInteractWithFluids(this))
            return (T) tank;
        
        return super.getCapability(capability, facing);
    }
    
    public static class InputOutputItemHandler extends ItemStackHandler
    {
        public InputOutputItemHandler()
        {
            super(2);
        }
        
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return slot == 0 ? 1 : super.getStackLimit(slot, stack);
        }
    }
    
}
