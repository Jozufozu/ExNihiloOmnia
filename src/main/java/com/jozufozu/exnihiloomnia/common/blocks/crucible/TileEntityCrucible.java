package com.jozufozu.exnihiloomnia.common.blocks.crucible;

import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCrucible extends TileEntity implements ITickable
{
    public CrucibleItemHandler itemHandler = new CrucibleItemHandler();
    public CrucibleFluidTank fluidHandler;
    
    /** How many mB of solid crucibles can have */
    public static int crucibleCapacity = 4000;
    
    /** How many mB of solid this crucible has */
    public int solidAmount;
    public int solidAmountLastTick;
    
    /** How many mB of fluid this crucible has */
    public int fluidAmount;
    public int fluidAmountLastTick;
    
    public float partialFluid;
    public float partialFluidLastTick;
    
    /** How hot this crucible currently is */
    public int currentHeatLevel;
    
    /** How hot this crucible's heat source is */
    public int sourceHeatLevel;
    
    /** How hot this crucible has to be for its contents to melt */
    public int requiredHeatLevel;
    
    public float meltingRatio;
    
    private int ticksExisted;
    
    public TileEntityCrucible()
    {
        fluidHandler = new CrucibleFluidTank(crucibleCapacity);
        fluidHandler.setCanFill(false);
        fluidHandler.setTileEntity(this);
    }
    
    @Override
    public void update()
    {
        solidAmountLastTick = solidAmount;
        fluidAmountLastTick = fluidAmount;
        partialFluidLastTick = partialFluid;

        if (ticksExisted % 100 == 0)
        {
            sourceHeatLevel = RegistryManager.getHeat(world.getBlockState(pos.down()));
            //Every 5 seconds, increase the heat by one if the heat source is hotter, decrease it by 1 if it is cooler, or leave it alone
            currentHeatLevel += Integer.signum(sourceHeatLevel - currentHeatLevel);
    
            markDirty();
        }
        
        if (solidAmount > 0 && currentHeatLevel >= requiredHeatLevel)
        {
            int meltingSpeed = currentHeatLevel - requiredHeatLevel;
            
            ItemStack solid = itemHandler.getStackInSlot(0);
            
            //We can't melt more than we have
            int melted = Math.min(solid.getCount(), meltingSpeed);
            
            //We can't melt more than we have space for
            melted = Math.min(melted, (int) Math.floor(fluidHandler.getCapacity() - fluidHandler.getFluidAmount() / meltingRatio));
            
            solid.shrink(melted);
            itemHandler.onContentsChanged(0);
    
            FluidStack fluid = fluidHandler.getFluid();
            
            if (fluid == null)
            {
                MeltingRecipe meltingRecipe = RegistryManager.getMelting(solid);
                
                if (meltingRecipe == null) //Something's fucked up
                {
                    itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    meltingRatio = 0;
                    requiredHeatLevel = 0;
                    
                    ticksExisted++;
                    return;
                }
                
                fluid = meltingRecipe.getOutput();
            }
            markDirty();
            
            fluidHandler.fillInternal(new FluidStack(fluid, (int)Math.floor(melted * meltingRatio)), true);
        }
        
        ticksExisted++;
    }
    
    @Override
    public void markDirty()
    {
        super.markDirty();
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }
    
    @Nonnull
    public ItemStack getSolidContents()
    {
        return this.itemHandler.getStackInSlot(0).copy();
    }
    
    @Nullable
    public FluidStack getFluidContents()
    {
        FluidStack fluid = this.fluidHandler.getFluid();
        
        if (fluid == null)
            return null;
        
        return fluid.copy();
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound crucibleData = new NBTTagCompound();
        crucibleData.setTag("solidContents", itemHandler.serializeNBT());
        crucibleData.setTag("fluidContents", fluidHandler.writeToNBT(new NBTTagCompound()));
    
        crucibleData.setInteger("currentHeat", currentHeatLevel);
        crucibleData.setInteger("sourceHeat", sourceHeatLevel);
        crucibleData.setInteger("requiredHeat", requiredHeatLevel);
        crucibleData.setFloat("meltingRatio", meltingRatio);
        
        compound.setTag("crucibleData", crucibleData);
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        
        NBTTagCompound crucibleData = compound.getCompoundTag("crucibleData");
        
        itemHandler.deserializeNBT(crucibleData.getCompoundTag("solidContents"));
        fluidHandler.readFromNBT(crucibleData.getCompoundTag("fluidContents"));
        
        fluidAmountLastTick = fluidAmount = fluidHandler.getFluidAmount();
        
        currentHeatLevel = crucibleData.getInteger("currentHeat");
        sourceHeatLevel = crucibleData.getInteger("sourceHeat");
        requiredHeatLevel = crucibleData.getInteger("requiredHeat");
        meltingRatio = crucibleData.getFloat("meltingRatio");
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability || CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability || super.hasCapability(capability, facing);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability)
            return (T) itemHandler;
        
        if (CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability)
            return (T) fluidHandler;
        
        return super.getCapability(capability, facing);
    }
    
    public class CrucibleItemHandler extends ItemStackHandler
    {
        public CrucibleItemHandler()
        {
            super(1);
        }
    
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return ItemStack.EMPTY;
        }
    
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (stack.isEmpty())
                return ItemStack.EMPTY;
    
            MeltingRecipe meltingRecipe = RegistryManager.getMelting(stack);
            
            if (meltingRecipe == null)
                return ItemStack.EMPTY;
            
            if (fluidAmount != 0 && !meltingRecipe.getOutput().isFluidEqual(fluidHandler.getFluid()))
                return ItemStack.EMPTY;
    
            ItemStack existing = this.stacks.get(slot);
    
            int inputVolume = meltingRecipe.getInputVolume();
            
            int allowedIn = Math.floorDiv(crucibleCapacity - existing.getCount(), inputVolume);
            
            allowedIn = Math.min(allowedIn, stack.getCount());
            
            ItemStack copy = stack.copy();
            copy.shrink(allowedIn);
            
            if (!simulate)
            {
                meltingRatio = (float) meltingRecipe.getOutput().amount / (float) inputVolume;
                
                if (fluidHandler.getFluid() == null)
                    fluidHandler.setFluid(new FluidStack(meltingRecipe.getOutput(), 0));
    
                if (existing.isEmpty())
                {
                    this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(stack, allowedIn * inputVolume));
                }
                else
                {
                    existing.grow(allowedIn * inputVolume);
                }
                onContentsChanged(slot);
            }
            
            return copy;
        }
    
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return crucibleCapacity;
        }
    
        @Override
        protected void onLoad()
        {
            solidAmountLastTick = solidAmount = getStackInSlot(0).getCount();
        }
    
        @Override
        protected void onContentsChanged(int slot)
        {
            solidAmount = getStackInSlot(slot).getCount();
        }
    }
    
    public class CrucibleFluidTank extends FluidTank
    {
        public CrucibleFluidTank(int capacity)
        {
            super(capacity);
        }
        
        @Override
        protected void onContentsChanged()
        {
            fluidAmount = getFluidAmount();
        }
    }
}
