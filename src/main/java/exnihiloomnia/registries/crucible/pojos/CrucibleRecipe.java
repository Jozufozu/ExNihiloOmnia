package exnihiloomnia.registries.crucible.pojos;

public class CrucibleRecipe {

	public String block;
	public int meta;
	public int solidVolume;
	public String fluid;
	public int fluidVolume;

	public CrucibleRecipe(String block, int meta, int solidAmount, String fluid, int fluidAmount)
	{
		this.block = block;
		this.meta = meta;
		this.solidVolume = solidAmount;

		this.fluid = fluid;
		this.fluidVolume = fluidAmount;
	}

	public String getBlock() {
		return block;
	}

	public int getMeta() {
		return meta;
	}

	public int getSolidVolume() {
		return solidVolume;
	}

	public String getFluid() {
		return fluid;
	}

	public int getFluidVolume() {
		return fluidVolume;
	}
	
}
