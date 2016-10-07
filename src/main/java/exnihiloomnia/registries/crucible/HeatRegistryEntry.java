package exnihiloomnia.registries.crucible;

import exnihiloomnia.registries.crucible.pojos.HeatValue;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class HeatRegistryEntry {
	private Block block;
	private int meta;
	private int value;
	
	public HeatRegistryEntry(Block block, int meta, int value) {
		this.block = block;
		this.meta = meta;
		this.value = value;
	}

	public static HeatRegistryEntry fromRecipe(HeatValue value) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(value.getBlock()));

		if (block != null)
			return new HeatRegistryEntry(block, value.getMeta(), value.getHeat());
		else
			return null;
	}

	public Block getBlock() {
		return block;
	}

	public int getMeta() {
		return meta;
	}

	public int getValue() {
		return value;
	}
}
