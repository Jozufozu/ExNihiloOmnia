package exnihiloomnia.registries.crucible.pojos;

public class CrucibleRecipe {
	public final String block;
	public final int meta;
	public final int solidVolume;
	public final String fluid;
	public final int fluidVolume;

	public CrucibleRecipe(String block, int meta, int solidAmount, String fluid, int fluidAmount) {
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
