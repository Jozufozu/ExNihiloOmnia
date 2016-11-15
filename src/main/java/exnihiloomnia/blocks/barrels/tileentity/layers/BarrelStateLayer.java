package exnihiloomnia.blocks.barrels.tileentity.layers;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class BarrelStateLayer extends TileEntity {
	protected BarrelState state;
	
	public BarrelState getState() {
		return state;
	}
	
	public void setState(BarrelState new_state) {
		String keyA = "";
		String keyB;

        //state is valid
		if (state != null) {
			keyA = this.state.getUniqueIdentifier();
		}

		if (new_state != null) {
			state = new_state;
		}
		else {
			state = BarrelStates.EMPTY;
		}
		
		keyB = this.state.getUniqueIdentifier();
		
		if (!keyA.equals(keyB)) {
			TileEntityBarrel barrel = (TileEntityBarrel)this;

			state.activate(barrel);
			barrel.resetTimer();
			barrel.requestSync();
		}
	}
	
	public void update() {
		//Update the barrel state object.
		if (this.state != null) {
			state.update((TileEntityBarrel) this);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.setState(BarrelStates.getState(compound.getString("state")));
	}
 
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (state != null) {
			compound.setString("state", state.getUniqueIdentifier());
		}
		
		return compound;
	}
}
