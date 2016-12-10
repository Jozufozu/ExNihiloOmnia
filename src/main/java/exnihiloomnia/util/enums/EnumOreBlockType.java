package exnihiloomnia.util.enums;

import exnihiloomnia.items.ENOItems;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum EnumOreBlockType implements IStringSerializable {
	GRAVEL("gravel", ENOItems.BROKEN_ORE),
	GRAVEL_NETHER("gravel_nether", ENOItems.BROKEN_ORE_NETHER),
	GRAVEL_ENDER("gravel_ender", ENOItems.BROKEN_ORE_ENDER),
	SAND("sand", ENOItems.CRUSHED_ORE),
	DUST("dust", ENOItems.POWDERED_ORE);

	private static final EnumOreBlockType[] META_LOOKUP = new EnumOreBlockType[values().length];

	private final String name;
	private final Item crafting;

	EnumOreBlockType(String name, Item crafting) {
		this.name = name;
		this.crafting = crafting;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Item getCrafting() {
		return crafting;
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
