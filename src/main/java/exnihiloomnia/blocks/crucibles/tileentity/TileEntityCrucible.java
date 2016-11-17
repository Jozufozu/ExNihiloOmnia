package exnihiloomnia.blocks.crucibles.tileentity;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityCrucible extends TileEntity implements ITickable{

	protected int updateTimer = 0;
	protected int updateTimerMax = 8; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;

    protected ItemStack item;
	protected int adjustedSpeed = 0;
	protected int solidContent = 0;
	protected int solidContentProcessed = 0;
	protected int solidContentMax = 200000;
	protected int solidFluidExchangeRate = 100;

    private ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            //ItemStack copy = stack.copy();
            CrucibleRegistryEntry meltable = CrucibleRegistry.getItem(stack);
            if (meltable != null) {
                if (canInsertItem(stack)) {
                    if (hasSpaceFor(meltable.getSolidVolume())) {
                        stack.stackSize--;

                        item = stack.copy();
                        addSolid(meltable.getSolidVolume());
                        sync();

                        return stack.stackSize == 0 ? null : stack;
                    }
                }
            }
            return stack;
        }
    };

    private FluidTank fluidTank = new FluidTank(8000) {

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public int getCapacity() {
            return 8000;
        }

        @Override
        public boolean canFill() {
            return false;
        }

        @Override
        protected void onContentsChanged() {
            markDirty();
            sync();
        }
    };

    public ItemStackHandler getItemHandler() {return itemHandler;}

    public FluidTank getTank() {return fluidTank;}

    public FluidStack getFluid() {return fluidTank.getFluid();}

	public void addSolid(int amount) {
		this.solidContent += (amount * 200);
	}
	
	public boolean hasSpaceFor(int amount) {
		return solidContent + (amount * 200) <= solidContentMax;
	}
	
	public double getSolidFullness() {
		return (double) this.solidContent / (double) this.solidContentMax;
	}
	
	public double getFluidFullness() {
	    if (getFluid() != null)
		    return (double)getFluid().amount / (double) getTank().getCapacity();
        else return 0;
	}
	
	public ItemStack getLastItemAdded()
	{
		return this.item;
	}

	public void setLastItemAdded(ItemStack item) {this.item = item;}

	public int getSolidContent()
	{
		return this.solidContent;
	}

	@Override
	public void update() {
		CrucibleRegistryEntry entry = CrucibleRegistry.getItem(item);
		//remove stuff if is no longer valid eg config change
        if (getFluid() != null && item != null) {
            if (entry == null) {
                this.solidContent = 0;
                this.item = null;
                if (!CrucibleRegistry.containsFluid(this.getFluid().getFluid()))
                    getTank().setFluid(null);
            }
        }
        
		//process solids
        if (getFluid() == null && item != null)
            getTank().setFluid(new FluidStack(entry.getFluid(), 0));

        if (this.solidContent <= 0)
            this.item = null;
		if (this.solidContent > 0 && getFluidFullness() < 1)
		{
			int speed = this.getMeltingSpeed();
			
			if (speed > solidContent * 2) {
				speed = solidContent / 2;
			}

			this.adjustedSpeed = speed;
			this.solidContentProcessed += speed;
			this.solidContent -= speed * 2;
		}
		
		//transfer solids to fluids
        if (getFluid() != null) {
            while (getFluid().amount < getTank().getCapacity() && this.solidContentProcessed >= solidFluidExchangeRate) {
                this.solidContentProcessed -= solidFluidExchangeRate;
                getFluid().amount += entry.getRatio();
            }
        }
		

		//Packet throttling
		if (!this.getWorld().isRemote) {
			if (updateTimerRunning) {
				updateTimer++;

				if (updateTimer > updateTimerMax) {
					updateTimer = 0;
					if (updateQueued) {
						updateQueued = false;

						getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					}
					else {
						updateTimerRunning = false;
					}
				}
			}
		}
		
		markDirty();
        this.getWorld().checkLight(this.getPos());
	}

	//Send update packets to each client.
	public void sync() {
		if (getWorld() != null && !getWorld().isRemote) {
			if (!updateTimerRunning) {
				updateTimerRunning = true;
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			}
			else {
				this.updateQueued = true;
			}
		}
	}
	
	public int getMeltingSpeed() {
		if (getWorld().isAirBlock(getPos().down()))
			return 0;
		
		IBlockState state = getWorld().getBlockState(getPos().down());

		return HeatRegistry.getHeatForState(state);
	}

	public double getTrueSpeed() {
        double speed = this.adjustedSpeed;
	    return speed != 0 ? getLastItemAdded() != null ? ((double) 4 / 15 * speed - (double) 1 / 3) * CrucibleRegistry.getItem(item).getRatio() : 0 : 0;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return !oldState.getBlock().equals(newState.getBlock());
	}

    public boolean canInsertItem(ItemStack stack) {
        if (CrucibleRegistry.isMeltable(Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()))) {
            FluidStack fluid = getFluid();
            CrucibleRegistryEntry block = CrucibleRegistry.getItem(stack);
            return item == null ? fluid == null ? hasSpaceFor(block.getSolidVolume()) : fluid.getFluid() == block.fluid && hasSpaceFor(block.getSolidVolume()) : item.getItem().equals(stack.getItem()) && hasSpaceFor(block.getSolidVolume());
        }

        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return ENOConfig.crucible_access ? (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) && (facing == EnumFacing.UP || facing == null) || super.hasCapability(capability, facing) : (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (ENOConfig.crucible_access) {
			if (facing == EnumFacing.UP || facing == null) {
				if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
					return (T) fluidTank;
				}
				if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
					return (T) itemHandler;
				}
			}
        }
        else {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return (T) fluidTank;
			}
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				return (T) itemHandler;
			}
		}
        return super.getCapability(capability, facing);
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
        getTank().readFromNBT(compound);
		
		this.solidContent = compound.getInteger("solid");
		this.solidContentProcessed = compound.getInteger("processed");
        NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);
        if (items.tagCount() > 0)
        {
            NBTTagCompound item = items.getCompoundTagAt(0);
            this.item = ItemStack.loadItemStackFromNBT(item);
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		getTank().writeToNBT(compound);

		compound.setInteger("solid", solidContent);
		compound.setInteger("processed", solidContentProcessed);
        NBTTagList items = new NBTTagList();
        if (item != null)
        {
            NBTTagCompound item = new NBTTagCompound();
            this.item.writeToNBT(item);
            items.appendTag(item);
        }
        compound.setTag("items", items);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
	}
}
