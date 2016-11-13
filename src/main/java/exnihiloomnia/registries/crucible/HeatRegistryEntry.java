package exnihiloomnia.registries.crucible;

import exnihiloomnia.registries.crucible.pojos.HeatValue;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class HeatRegistryEntry {
	private IBlockState input;
	private EnumMetadataBehavior behavior;
	private int value;
	
	public HeatRegistryEntry(IBlockState state, EnumMetadataBehavior behavior, int value) {
		this.input = state;
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
