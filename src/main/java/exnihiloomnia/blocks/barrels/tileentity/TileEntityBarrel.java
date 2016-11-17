package exnihiloomnia.blocks.barrels.tileentity;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.layers.BarrelStateLayer;
import exnihiloomnia.util.Color;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityBarrel extends BarrelStateLayer implements ITickable {
	private TileEntityBarrel getBarrel() {return this;}

	protected int luminosity = 0;
	protected int volume = 0;
	protected int MAX_VOLUME = 1000;
	protected Color color = Color.WHITE;
	
	protected int generalTimer = 0;
	protected int generalTimerMax = 0;
	
	protected int updateTimer = 0;
	protected int updateTimerMax = 8; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;

    protected ArrayList<ItemStack> output = new ArrayList<ItemStack>();
    protected ItemStack contents = null;

	private FluidTank fluidTank = new FluidTank(Fluid.BUCKET_VOLUME) {
		@Override
		public int fill(FluidStack resource, boolean doFill)  {
			if (resource == null || state == null || !state.canManipulateFluids(getBarrel()))
			{
				return 0;
			}

			if (!doFill)
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
				setState(BarrelStates.FLUID);
				setColor(fluid.getFluid().getColor());

				return fluid.amount;
			}

			if (!fluid.isFluidEqual(resource))
			{
				return 0;
			}

			int avaliable = capacity - fluid.amount;

			if (resource.amount < avaliable)
			{
				fluid.amount += resource.amount;
				avaliable = resource.amount;
				requestSync();
			}
			else
			{
				fluid.amount = capacity;
				requestSync();
			}

			return avaliable;
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			BarrelState state = getState();

			if (fluid == null || state == null || !state.canManipulateFluids(getBarrel()) || !canDrainFluidType(fluid))
				return null;
            else {
				FluidStack drain = drainInternal(maxDrain, doDrain);

				if (fluid == null) {
					setState(BarrelStates.EMPTY);
					setColor(Color.WHITE);
				}

				return drain;
			}
		}

        @Override
        protected void onContentsChanged() {
            markDirty();
            requestSync();
        }
	};

	private ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        public ItemStack getStackInSlot(int index) {
            if (index == 0) {
                if (contents != null && getState().canExtractContents(getBarrel())) {
                    return contents;
                }
                else if (output.size() > 0) {
                    return output.get(0);
                }
            }

            return null;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

            if (stack == null || stack.getItem() == null) {

                if (contents != null) {
                    setContents(null);
                }
                else if (slot == 0 && output.size() > 0) {
                    output.remove(0);
                }
            }
            else if (slot == 1 && getState().canUseItem(getBarrel(), stack))
                getState().useItem(null, null, getBarrel(), stack);

            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int count, boolean simulate) {
            ItemStack item = getStackInSlot(slot);

            if (item != null && count > 0)  {
                if (item.stackSize <= count)  {
                    if (!simulate)
                        setInventorySlotContents(slot, null);
                }
                else {
                    item = item.splitStack(count);
                }
            }

            return item;
        }
	};

	public FluidTank getFluidTank() {return fluidTank;}

    public ItemStackHandler getItemHandler() {return itemHandler;}

    public FluidStack getFluid() {
        return getFluidTank().getFluid();
    }

    public void setInventorySlotContents(int index, ItemStack stack)  {
        if (stack == null || stack.getItem() == null) {
            if (contents != null) {
                setContents(null);
            }
            else if (index == 0 && output.size() > 0) {
                output.remove(0);
            }
        }
        else {
            if (index == 1) {
                TileEntityBarrel barrel = this;

                getState().useItem(null, null, barrel, stack);
            }
        }
    }

    public void addOutput(ItemStack item) {
        if (item != null && item.stackSize > 0) {
            output.add(item);
        }
    }

    public void setContents(ItemStack item) {
        if (item != null) {
            contents = item;
            requestSync();
        }
        else {
            if (contents != null) {
                contents = null;
                state.onExtractContents(this);
            }
        }
    }

    public ItemStack getContents() {
        return contents;
    }

    public boolean canExtractItem(int index) {
        if (index == 0) {
            if (output.size() > 0) {
                return true;
            }
            else if (contents != null && state.canExtractContents(this)) {
                return true;
            }
        }

        return false;
    }

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return !oldState.getBlock().equals(newState.getBlock());
	}

	public void startTimer(int maxTicks) {
		generalTimer = 0;
		generalTimerMax = maxTicks;
	}
	
	public int getTimerTime() {
		return generalTimer;
	}
	
	public double getTimerStatus() {
		if (generalTimerMax == 0) {
			return -1.0d;
		}
		
		if (generalTimer >= generalTimerMax) {
			return 1.0d;
		}
		else {
			return (double)generalTimer / (double)generalTimerMax;
		}
	}
	
	public void resetTimer() {
		generalTimer = 0;
		generalTimerMax = 0;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (state != null) {
			setLuminosity(state.getLuminosity(this));
		}
		
		//Update timer used by states.
		if (generalTimerMax != 0 && generalTimer < generalTimerMax) {
			generalTimer++;
		}
		
		//Update packet throttling system
		if (!this.getWorld().isRemote && updateTimerRunning) {
			updateTimer++;
			
			if (updateTimer > updateTimerMax) {
				updateTimer = 0;
				
				if (updateQueued) {
					updateQueued = false;
					this.getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				}
				else {
					updateTimerRunning = false;
				}
			}
		}
		
		markDirty();
	}
	
	public void requestSync() {
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
	
	public int getLuminosity() {
		return luminosity;
	}
	
	//Call this as much as you want. 
	//The lighting calculations don't fire unless the value is actually changed.
	public void setLuminosity(int level) {
		if (luminosity != level) {
			luminosity = level;
			
			if (getWorld() != null) {
				getWorld().checkLight(getPos());
			}
		}
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
		
		if (this.volume > this.getVolumeMax()) {
			this.volume = getVolumeMax();
		}
	}
	
	public int getVolumeMax() {
		return this.MAX_VOLUME;
	}
	
	public double getVolumeProportion() {
		return (double) this.getVolume() / (double) this.getVolumeMax();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void setColor(int color) {
		this.color = new Color(color);
	}

	public boolean isWooden() {
		return getWorld().getBlockState(pos).getMaterial() == Material.WOOD;
	}

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canManipulateFluids(this)) || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && state.canManipulateFluids(this)) {
            return (T) fluidTank;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) itemHandler;
        return super.getCapability(capability, facing);
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
        getFluidTank().readFromNBT(compound);

		generalTimer = compound.getInteger("timer");
		generalTimerMax = compound.getInteger("timermax");
		setLuminosity(compound.getInteger("luminosity"));
		volume = compound.getInteger("volume");
		color = new Color(compound.getInteger("color"));

        NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);

        for (int x = 0; x < items.tagCount(); x++) {
            NBTTagCompound item = items.getCompoundTagAt(x);
            output.add(ItemStack.loadItemStackFromNBT(item));
        }

        NBTTagList content = compound.getTagList("content", Constants.NBT.TAG_COMPOUND);

        if (content.tagCount() > 0) {
            NBTTagCompound item = content.getCompoundTagAt(0);
            contents = ItemStack.loadItemStackFromNBT(item);
        }
	}
 
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
        getFluidTank().writeToNBT(compound);
		
		compound.setInteger("timer", generalTimer);
		compound.setInteger("timermax", generalTimerMax);
		compound.setInteger("luminosity", getLuminosity());
		compound.setInteger("volume", volume);
		compound.setInteger("color", this.color.toInt());

        NBTTagList items = new NBTTagList();

        for (ItemStack itemStack : output) {
            NBTTagCompound item = new NBTTagCompound();
            itemStack.writeToNBT(item);
            items.appendTag(item);
        }

        compound.setTag("items", items);

        NBTTagList content = new NBTTagList();

        if (contents != null) {
            NBTTagCompound item = new NBTTagCompound();
            contents.writeToNBT(item);
            content.appendTag(item);
        }

        compound.setTag("content", content);
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
