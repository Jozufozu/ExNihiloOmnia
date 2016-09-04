package exnihiloomnia.blocks.crucibles.tileentity.layers;

import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class CrucibleFluidLayer extends TileEntity implements IFluidHandler{
	public static final int fluidCapacity = 8000;
	protected FluidStack fluid = null;

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (fluid == null || resource == null || !resource.isFluidEqual(fluid))
        {
            return null;
        }
		else
		{
	        return drain(resource.amount, doDrain);
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluid == null || maxDrain == 0)
        {
            return null;
        }
		else
		{
	        int drained = maxDrain;
	        
	        if (fluid.amount <= drained)
	        {
	            drained = fluid.amount;
	        }

	        FluidStack stack = new FluidStack(fluid, drained);
	        
	        if (doDrain)
	        {
	            fluid.amount -= drained;
	            ((TileEntityCrucible)this).sync();
	        }
	        
	        return stack;
		}
	}

    @Nullable
    public FluidStack getFluid()
    {
        return fluid;
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return new IFluidTankProperties[] { new FluidTankProperties(getFluid(), fluidCapacity, false, true) };
    }

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.getBoolean("fluid"))
		{
			fluid = FluidStack.loadFluidStackFromNBT(compound);
		}
	}
 
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setBoolean("fluid", fluid != null);
		if (fluid != null)
		{
			fluid.writeToNBT(compound);
		}
		return compound;
	}
}
