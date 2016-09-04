package exnihiloomnia.blocks.barrels.tileentity.layers;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class BarrelFluidLayer extends BarrelStateLayer implements IFluidTank, IFluidHandler
{
	private static final int CAPACITY = 1000;
	
	protected FluidStack fluid;
	
	//IFluidTank
	@Override
	public FluidStack getFluid() 
	{
		return fluid;
	}

	@Override
	public int getFluidAmount()
	{
		if (fluid == null)
        {
            return 0;
        }
		
        return fluid.amount;
	}

	@Override
	public int getCapacity() 
	{
		return CAPACITY;
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return new FluidTankInfo(this);
	}

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return new IFluidTankProperties[] { new FluidTankProperties(getFluid(), CAPACITY, false, true) };
    }

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		TileEntityBarrel barrel = (TileEntityBarrel)this;
		BarrelState state = barrel.getState();
		
        if (resource == null || state == null || !state.canManipulateFluids(barrel))
        {
            return 0;
        }

        if (!doFill)
        {
            if (fluid == null)
            {
                return Math.min(CAPACITY, resource.amount);
            }

            if (!fluid.isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(CAPACITY - fluid.amount, resource.amount);
        }

        if (fluid == null)
        {
            fluid = new FluidStack(resource, Math.min(CAPACITY, resource.amount));
            barrel.setState(BarrelStates.FLUID);

            return fluid.amount;
        }

        if (!fluid.isFluidEqual(resource))
        {
            return 0;
        }
        
        int avaliable = CAPACITY - fluid.amount;

        if (resource.amount < avaliable)
        {
            fluid.amount += resource.amount;
            avaliable = resource.amount;
            barrel.requestSync();
        }
        else
        {
            fluid.amount = CAPACITY;
            barrel.requestSync();
        }

        return avaliable;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        TileEntityBarrel barrel = (TileEntityBarrel)this;
		BarrelState state = barrel.getState();
		
        if (fluid == null || state == null || !state.canManipulateFluids(barrel))
        {
            return null;
        }

        int drained = maxDrain;
        if (fluid.amount < drained)
        {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            fluid.amount -= drained;
            if (fluid.amount <= 0)
            {
                fluid = null;
                setState(BarrelStates.EMPTY);
            }
            else
            {
            	barrel.requestSync();
            }
        }
        return stack;
    }
    
    public void clearFluid()
    {
    	this.fluid = null;
    }
    
    public void transformFluidTo(FluidStack input)
    {
    	this.fluid = input;
    }

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.getBoolean("FLUID"))
		{
			fluid = FluidStack.loadFluidStackFromNBT(compound);
		}
	}
 
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setBoolean("FLUID", fluid != null);
		if (fluid != null)
		{
			fluid.writeToNBT(compound);
		}
		return  compound;
	}

	/* IFluidHandler */
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(getFluid()))
        {
            return null;
        }
        
        return drain(resource.amount, doDrain);
    }
}
