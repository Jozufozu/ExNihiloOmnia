package exnihiloomnia.blocks.barrels.tileentity;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.layers.BarrelInventoryLayer;
import exnihiloomnia.blocks.barrels.tileentity.layers.BarrelStateLayer;
import exnihiloomnia.util.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityBarrel extends BarrelInventoryLayer implements ITickable {
	private TileEntityBarrel getBarrel() {return this;}

	protected int luminosity = 0;
	protected int volume = 0;
	protected int MAX_VOLUME = 1000;
	protected Color color = new Color("FFFFFF");
	
	protected int generalTimer = 0;
	protected int generalTimerMax = 0;
	
	protected int updateTimer = 0;
	protected int updateTimerMax = 8; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;

	private FluidTank fluidTank = new FluidTank(Fluid.BUCKET_VOLUME) {
		@Override
		public int fill(FluidStack resource, boolean doFill)  {
			BarrelState state = getState();

			if (resource == null || state == null || !state.canManipulateFluids(getBarrel())) {
				return 0;
			}

			if (!doFill) {
				if (fluid == null) {
					return Math.min(capacity, resource.amount);
				}

				if (!fluid.isFluidEqual(resource)) {
					return 0;
				}

				return Math.min(capacity - fluid.amount, resource.amount);
			}

			if (fluid == null) {
				fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
				setState(BarrelStates.FLUID);

				return fluid.amount;
			}

			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}

			int avaliable = capacity - fluid.amount;

			if (resource.amount < avaliable) {
				fluid.amount += resource.amount;
				avaliable = resource.amount;
				requestSync();
			}
			else {
				fluid.amount = capacity;
				requestSync();
			}

			return avaliable;
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			BarrelState state = getState();

			if (fluid == null || state == null || !state.canManipulateFluids(getBarrel())) {
				return null;
			}

			int drained = maxDrain;

			if (fluid.amount < drained) {
				drained = fluid.amount;
			}

			FluidStack stack = new FluidStack(fluid, drained);

			if (doDrain) {
				fluid.amount -= drained;

				if (fluid.amount <= 0) {
					fluid = null;
					setState(BarrelStates.EMPTY);
				}
				else {
					requestSync();
				}
			}

			return stack;
		}
	};

	public FluidTank getFluidTank() {return fluidTank;}

    public FluidStack getFluid() {
        return getFluidTank().getFluid();
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
		if (!this.worldObj.isRemote && updateTimerRunning) {
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

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) fluidTank;
        }
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
