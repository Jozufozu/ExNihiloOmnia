package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import com.jozufozu.exnihiloomnia.common.ModConfig;
import com.jozufozu.exnihiloomnia.common.util.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntity implements ITickable
{
    public final int compostCapacity = ModConfig.blocks.barrel.compostCapacity;
    public final int fluidCapacity = ModConfig.blocks.barrel.fluidCapacity;
    
    public int compostAmount;
    public int compostAmountLastTick;
    
    public Color color;
    
    public int fluidAmount;
    public int fluidAmountLastTick;
    
    public int timer;
    public int timerLastTick;
    
    private BarrelState state = BarrelStates.EMPTY;
    
    private BarrelFluidHandler fluidHandler = new BarrelFluidHandler(fluidCapacity);
    private BarrelItemHandler itemHandler = new BarrelItemHandler();
    
    private int updateTimer;
    private boolean updateNeeded;
    
    @Override
    public void update()
    {
        this.timerLastTick = this.timer;
        this.fluidAmountLastTick = this.fluidAmount;
        this.compostAmountLastTick = this.compostAmount;
        
        state.update(this);
        
        this.fluidAmount = this.fluidHandler.getFluidAmount();
        
        if (!world.isRemote)
        {
            if (updateNeeded && updateTimer == 0)
            {
                sync();
            }
            updateTimer--;
        }
    }
    
    private void sync()
    {
        BlockPos pos = getPos();
        IBlockState blockState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, blockState, blockState, 3);
        
        updateNeeded = false;
    }
    
    public void sync(boolean now)
    {
        if (now)
        {
            updateNeeded = true;
            updateTimer = 0;
        }
        else if (!updateNeeded)
        {
            updateNeeded = true;
            updateTimer = 10;
        }
    }
    
    @Nonnull
    public BarrelState getState()
    {
        return state;
    }
    
    public void setState(BarrelState state)
    {
        BarrelState last = this.state;
        this.state = state;
        
        this.state.activate(this, last);
        sync(true);
    }
    
    @Nullable
    public FluidStack getFluid()
    {
        return fluidHandler.getFluid();
    }
    
    public void setFluid(@Nullable FluidStack fluid)
    {
        fluidHandler.setFluid(fluid);
        sync(true);
    }
    
    public void setItem(ItemStack content)
    {
        itemHandler.setStackInSlot(0, content);
        sync(true);
    }
    
    public ItemStack getItem()
    {
        return itemHandler.getStackInSlot(0);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound barrelTag = new NBTTagCompound();
        
        barrelTag.setString("state", this.state.id.toString());
        
        if (this.color != null)
        {
            barrelTag.setInteger("color", this.color.toInt());
        }
        
        barrelTag.setTag("itemHandler", itemHandler.serializeNBT());
        barrelTag.setTag("fluidHandler", fluidHandler.writeToNBT(new NBTTagCompound()));
        
        barrelTag.setInteger("compostAmount", this.compostAmount);
        
        compound.setTag("barrel", barrelTag);
        
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound barrelTag = compound.getCompoundTag("barrel");
        
        if (barrelTag.hasKey("color"))
        {
            this.color = new Color(barrelTag.getInteger("color"), true);
        }
        
        this.setState(BarrelState.STATES.getOrDefault(new ResourceLocation(barrelTag.getString("state")), BarrelStates.EMPTY));
        
        this.itemHandler.deserializeNBT(barrelTag.getCompoundTag("itemHandler"));
        this.fluidHandler.readFromNBT(barrelTag.getCompoundTag("fluidHandler"));
    
        this.compostAmount = barrelTag.getInteger("compostAmount");
        
        super.readFromNBT(compound);
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
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && state.canInteractWithItems(this) ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canInteractWithFluids(this))
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
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && state.canInteractWithItems(this))
            return (T) itemHandler;
        
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canInteractWithFluids(this))
            return (T) fluidHandler;
        
        return super.getCapability(capability, facing);
    }
    
    private TileEntityBarrel getBarrel()
    {
        return this;
    }
    
    public class BarrelItemHandler extends ItemStackHandler
    {
        public BarrelItemHandler()
        {
            super(1);
        }
    
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            if (stack.isEmpty())
                return ItemStack.EMPTY;
            
            if (!state.canInteractWithItems(getBarrel()) && !state.canUseItem(getBarrel(), null, null, stack))
            {
                return stack;
            }
    
            EnumInteractResult enumInteractResult = state.onUseItem(getBarrel(), null, null, stack);
            
            if (enumInteractResult == EnumInteractResult.CONSUME)
            {
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
            }
            else if (enumInteractResult == EnumInteractResult.PASS)
            {
                return stack;
            }
    
            validateSlotIndex(slot);
    
            ItemStack existing = this.stacks.get(slot);
    
            int limit = getStackLimit(slot, stack);
    
            if (!existing.isEmpty())
            {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack;
        
                limit -= existing.getCount();
            }
    
            if (limit <= 0)
                return stack;
    
            boolean reachedLimit = stack.getCount() > limit;
    
            if (!simulate)
            {
                if (existing.isEmpty())
                {
                    this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                }
                else
                {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged(slot);
            }
    
            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
        }
    
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if (!state.canExtractItems(getBarrel()))
            {
                return ItemStack.EMPTY;
            }
    
            ItemStack out = super.extractItem(slot, amount, simulate);
    
            if (!simulate)
            {
                setState(BarrelStates.EMPTY);
            }
            
            return out;
        }
    
        @Override
        protected void onContentsChanged(int slot)
        {
            sync(true);
        }
    }
    
    public class BarrelFluidHandler extends FluidTank
    {
        public BarrelFluidHandler(int capacity)
        {
            super(capacity);
            this.setTileEntity(getBarrel());
        }
    
        @Override
        public int fillInternal(FluidStack resource, boolean doFill)
        {
            if (resource == null || resource.amount <= 0 || !state.canInteractWithFluids(getBarrel()) || !state.canFillFluid(getBarrel(), resource))
            {
                return 0;
            }
    
            if (!doFill || state.onFillFluid(getBarrel(), resource) == EnumInteractResult.CONSUME)
            {
                if (fluid == null)
                {
                    return Math.min(capacity, resource.amount);
                }
        
                if (!fluid.isFluidEqual(resource))
                {
                    return 0;
                }
        
                return Math.min(capacity - fluid.amount, resource.amount);
            }
    
            if (fluid == null)
            {
                fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
        
                onContentsChanged();
        
                if (tile != null)
                {
                    FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, fluid.amount));
                }
                return fluid.amount;
            }
    
            if (!fluid.isFluidEqual(resource))
            {
                return 0;
            }
            int filled = capacity - fluid.amount;
    
            if (resource.amount < filled)
            {
                fluid.amount += resource.amount;
                filled = resource.amount;
            }
            else
            {
                fluid.amount = capacity;
            }
    
            onContentsChanged();
    
            if (tile != null)
            {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, filled));
            }
            return filled;
        }
    
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            if (!state.canInteractWithFluids(getBarrel()))
            {
                return null;
            }
            
            FluidStack out = super.drain(maxDrain, doDrain);
    
            if (doDrain)
            {
                setState(BarrelStates.EMPTY);
            }
            
            return out;
        }
    
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            if (!state.canExtractFluids(getBarrel()))
            {
                return null;
            }
            
            FluidStack out = super.drain(resource, doDrain);
    
            if (doDrain)
            {
                setState(BarrelStates.EMPTY);
            }
            
            return out;
        }
    
        @Override
        protected void onContentsChanged()
        {
            sync(true);
        }
    }
}
