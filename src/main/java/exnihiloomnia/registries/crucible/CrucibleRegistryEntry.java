package exnihiloomnia.registries.crucible;

import exnihiloomnia.registries.crucible.pojos.CrucibleRecipe;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CrucibleRegistryEntry {
	public Block block;
	public int meta;
	public int solidVolume;
	public Fluid fluid;
	public int fluidVolume;

	public CrucibleRegistryEntry(Block block, int meta, int solidAmount, Fluid fluid, int fluidAmount)
	{
		this.block = block;
		this.meta = meta;
		this.solidVolume = solidAmount;

		this.fluid = fluid;
		this.fluidVolume = fluidAmount;
	}

    public static CrucibleRegistryEntry fromRecipe(CrucibleRecipe recipe)
    {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getBlock()));
        Fluid fluid = FluidRegistry.getFluid(recipe.getFluid());

        if (block != null && fluid != null)
            return new CrucibleRegistryEntry(block, recipe.getMeta(), recipe.getSolidVolume(), fluid, recipe.getFluidVolume());
        else
            return null;
    }

	public float getRatio() {return (float) fluidVolume / solidVolume;}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public int getMeta() {
		return meta;
	}

	public void setMeta(int meta) {
		this.meta = meta;
	}

	public int getSolidVolume() {
		return solidVolume;
	}

	public void setSolidVolume(int solidVolume) {
		this.solidVolume = solidVolume;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public int getFluidVolume() {
		return fluidVolume;
	}

	public void setFluidVolume(int fluidVolume) {
		this.fluidVolume = fluidVolume;
	}
}
