package exnihiloomnia.registries.crucible;

import exnihiloomnia.registries.crucible.pojos.CrucibleRecipe;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CrucibleRegistryEntry {
	public IBlockState input;
	public EnumMetadataBehavior behavior;
	public int solidVolume;
	public Fluid fluid;
	public int fluidVolume;

	public CrucibleRegistryEntry(IBlockState input, EnumMetadataBehavior behavior, int solidAmount, Fluid fluid, int fluidAmount) {
		this.input = input;
		this.solidVolume = solidAmount;

		this.fluid = fluid;
		this.fluidVolume = fluidAmount;
	}

	public String getKey() {
		String s = Block.REGISTRY.getNameForObject(input.getBlock()).toString();

		if (behavior == EnumMetadataBehavior.IGNORED) {
			return s + ":*";
		}
		else {
			return s + ":" + input.getBlock().getMetaFromState(input);
		}
	}

    public static CrucibleRegistryEntry fromRecipe(CrucibleRecipe recipe) {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getBlock()));
        Fluid fluid = FluidRegistry.getFluid(recipe.getFluid());

        if (block != null && fluid != null)
            return new CrucibleRegistryEntry(block.getStateFromMeta(recipe.getMeta()), recipe.getMeta() == -1 ? EnumMetadataBehavior.IGNORED : EnumMetadataBehavior.SPECIFIC, recipe.getSolidVolume(), fluid, recipe.getFluidVolume());
        else
            return null;
    }

    public CrucibleRecipe toRecipe() {
		return new CrucibleRecipe(Block.REGISTRY.getNameForObject(this.getInput().getBlock()).toString(),  this.getBehavior() == EnumMetadataBehavior.SPECIFIC ? this.getInput().getBlock().getMetaFromState(this.getInput()) : -1, getSolidVolume(), FluidRegistry.getFluidName(getFluid()), getFluidVolume());
	}

	public float getRatio() {
		return (float) fluidVolume / solidVolume;
	}

	public IBlockState getInput() {
		return input;
	}

	public EnumMetadataBehavior getBehavior() {
		return behavior;
	}

	public int getSolidVolume() {
		return solidVolume;
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
