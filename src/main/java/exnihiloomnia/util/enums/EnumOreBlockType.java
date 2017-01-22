package exnihiloomnia.util.enums;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum EnumOreBlockType implements IStringSerializable {
	GRAVEL("gravel"),
	GRAVEL_NETHER("gravel_nether"),
	GRAVEL_ENDER("gravel_ender"),
	SAND("sand"),
	DUST("dust");

	private static final EnumOreBlockType[] META_LOOKUP = new EnumOreBlockType[values().length];

	private final String name;

	EnumOreBlockType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Item getCrafting() {
		switch (this) {
			case GRAVEL:
				return ENOItems.BROKEN_ORE;
			case GRAVEL_NETHER:
				return ENOItems.BROKEN_ORE_NETHER;
			case GRAVEL_ENDER:
				return ENOItems.BROKEN_ORE_ENDER;
			case SAND:
				return ENOItems.CRUSHED_ORE;
			case DUST:
				return ENOItems.POWDERED_ORE;
			default:
				return null;
		}
	}

	public Item getSmashing() {
		switch (this) {
			case GRAVEL:
				return ENOItems.CRUSHED_ORE;
			case GRAVEL_NETHER:
				return ENOItems.CRUSHED_ORE;
			case GRAVEL_ENDER:
				return ENOItems.CRUSHED_ORE;
			case SAND:
				return ENOItems.POWDERED_ORE;
			case DUST:
				return null;
			default:
				return null;
		}
	}

	public ModelResourceLocation getLocation() {
		return new ModelResourceLocation(ENO.MODID + ":ore_" + this.name);
	}

	public static EnumOreBlockType fromMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	static {
		for (EnumOreBlockType ore : EnumOreBlockType.values()) {
			META_LOOKUP[ore.ordinal()] = ore;
		}
	}
}
