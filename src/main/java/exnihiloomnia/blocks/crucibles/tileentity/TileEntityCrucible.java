package exnihiloomnia.blocks.crucibles.tileentity;

import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import exnihiloomnia.blocks.crucibles.tileentity.layers.CrucibleInventoryLayer;

public class TileEntityCrucible extends CrucibleInventoryLayer implements ITickable{
	protected int updateTimer = 0;
	protected int updateTimerMax = 8; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;

	protected int adjustedSpeed = 0;
	protected int solidContent = 0;
	protected int solidContentProcessed = 0;
	protected int solidContentMax = 200000;
	protected int solidFluidExchangeRate = 100;
	
	private int luminosity = 0;
	
	public void addSolid(int amount)
	{
		this.solidContent += (amount * 200);
	}
	
	public boolean hasSpaceFor(int amount)
	{
		return solidContent + (amount * 200) <= solidContentMax;
	}
	
	public double getSolidFullness()
	{
		return ((double)this.solidContent / (double)this.solidContentMax);
	}
	
	public double getFluidFullness()
	{
	    if (getFluid() != null)
		    return (double)this.fluid.amount / (double) fluidCapacity;
        else return 0;
	}
	
	public ItemStack getLastItemAdded()
	{
		return this.lastItemAdded;
	}
	
	public FluidStack getCurrentFluid()
	{
		return this.fluid;
	}
	public int getSolidContent()
	{
		return this.solidContent;
	}

	@Override
	public void update() 
	{
		//remove stuff if is no longer valid
        if (fluid !=null && lastItemAdded != null) {
            if (!CrucibleRegistry.containsItem(Block.getBlockFromItem(getLastItemAdded().getItem()), getLastItemAdded().getMetadata())) {
                this.solidContent = 0;
                this.lastItemAdded = null;
                if (!CrucibleRegistry.containsFluid(this.getFluid().getFluid()))
                    this.fluid = null;
            }
        }
		//process solids
        if (this.fluid == null && lastItemAdded != null)
            this.fluid = new FluidStack(CrucibleRegistry.getItem(Block.getBlockFromItem(getLastItemAdded().getItem()), getLastItemAdded().getMetadata()).fluid, 0);

        if (this.solidContent <= 0)
            this.lastItemAdded = null;
		if (this.solidContent > 0 && getFluidFullness() < 1)
		{
			int speed = this.getMeltingSpeed();
			
			if (speed > solidContent * 2)
			{
				speed = solidContent / 2;
			}

			this.adjustedSpeed = speed;
			this.solidContentProcessed += speed;
			this.solidContent -= speed * 2;
		}
		
		//transfer solids to fluids
        if (this.fluid != null) {
            while (this.fluid.amount < fluidCapacity && this.solidContentProcessed >= solidFluidExchangeRate) {
                this.solidContentProcessed -= solidFluidExchangeRate;
                this.fluid.amount += CrucibleRegistry.getItem(Block.getBlockFromItem(lastItemAdded.getItem()), lastItemAdded.getMetadata()).getRatio();
            }
        }
		

		//Packet throttling
		if (!this.worldObj.isRemote)
		{
			if (updateTimerRunning)
			{
				updateTimer++;

				if (updateTimer > updateTimerMax)
				{
					updateTimer = 0;
					if (updateQueued)
					{
						updateQueued = false;

						getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					}
					else
					{
						updateTimerRunning = false;
					}
				}
			}
		}
		markDirty();
        this.getWorld().checkLight(this.getPos());
	}

	//Send update packets to each client.
	public void sync()
	{
		if (getWorld() != null && !getWorld().isRemote)
		{
			if (!updateTimerRunning)
			{
				updateTimerRunning = true;
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			}
			else
			{
				this.updateQueued = true;
			}
		}
	}
	
	public int getMeltingSpeed()
	{
		if (getWorld().isAirBlock(getPos().down()))
			return 0;
		
		IBlockState state = getWorld().getBlockState(getPos().down());

		if (HeatRegistry.containsItem(state.getBlock(), state.getBlock().getMetaFromState(state)))
		{
			return HeatRegistry.getItem(state.getBlock(), state.getBlock().getMetaFromState(state)).value;
		}
		else
		{
			return 0;
		}
	}

	public double getTrueSpeed() {
        double speed = this.adjustedSpeed;
	    return speed != 0 ? getLastItemAdded() != null ? ((double) 4 / 15 * speed - (double) 1 / 3) * CrucibleRegistry.getItem(Block.getBlockFromItem(getLastItemAdded().getItem()), getLastItemAdded().getMetadata()).getRatio() : 0 : 0;
	}
	
	public int getLuminosity()
	{
		return this.luminosity;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) 
	{
		return !oldState.getBlock().equals(newState.getBlock());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		this.solidContent = compound.getInteger("solid");
		this.solidContentProcessed = compound.getInteger("processed");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setInteger("solid", solidContent);
		compound.setInteger("processed", solidContentProcessed);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
	}
}
