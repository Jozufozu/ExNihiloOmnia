package exnihiloomnia.registries.crucible;

import exnihiloomnia.registries.crucible.pojos.HeatValue;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class HeatRegistryEntry {
	private final IBlockState input;
	private final EnumMetadataBehavior behavior;
	private final int value;
	
	public HeatRegistryEntry(IBlockState state, EnumMetadataBehavior behavior, int value) {
		this.input = state;
		this.behavior = behavior;
		this.value = value;
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

	public static HeatRegistryEntry fromRecipe(HeatValue value) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(value.getBlock()));

		if (block != null)
			return new HeatRegistryEntry(block.getStateFromMeta(value.getMeta()), value.getMeta() == -1 ? EnumMetadataBehavior.IGNORED : EnumMetadataBehavior.SPECIFIC, value.getHeat());
		else
			return null;
	}

	public HeatValue toRecipe() {
		return new HeatValue(Block.REGISTRY.getNameForObject(this.getInput().getBlock()).toString(), this.getBehavior() == EnumMetadataBehavior.SPECIFIC ? this.getInput().getBlock().getMetaFromState(this.getInput()) : -1, this.getHeat());
	}

	public IBlockState getInput() {
		return input;
	}

	public EnumMetadataBehavior getBehavior() {
		return behavior;
	}

	public int getHeat() {
		return value;
	}
}
