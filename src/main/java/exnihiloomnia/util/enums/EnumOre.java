package exnihiloomnia.util.enums;

import exnihiloomnia.util.Color;
import net.minecraft.util.IStringSerializable;

public enum EnumOre implements IStringSerializable {
	IRON(0, "iron", new Color("F2AB7C"), true, true, false, 30),
	GOLD(1, "gold", new Color("FFD000"), true, true, false, 10),
	TIN(2, "tin", new Color("ABC9B6"), true, false, true, 15),
	COPPER(3, "copper", new Color("F46E00"), true, true, false, 20),
	LEAD(4, "lead", new Color("2D2563"), true, false, true, 10),
	SILVER(5, "silver", new Color("8CC9FF"), true, false, true, 5),
	NICKEL(6, "nickel", new Color("BAB877"), true, true, false, 10),
	PLATINUM(7, "platinum", new Color("38CDFF"), true, false, true, 2),
	ALUMINUM(8, "aluminum", new Color("FFC7C7"), true, false, true, 20),
	OSMIUM(9, "osmium", new Color("608FC4"), true, false, false, 20),
	ARDITE(10, "ardite", new Color("FF4D00"), false, true, false, 2),
	COBALT(11, "cobalt", new Color("0B91FF"), false, true, false, 2);

	private final int meta;
	private final String name;
	private final Color color;
	private final int rarity;
	private final boolean hasGravel;
	private final boolean hasNether;
	private final boolean hasEnd;

	private static final EnumOre[] META_LOOKUP = new EnumOre[values().length];

	EnumOre(int meta, String name, Color color, boolean hasGravel, boolean hasNether, boolean hasEnd, int rarity) {
		this.meta = meta;
		this.name = name;
		this.color = color;
		this.hasGravel = hasGravel;
		this.hasEnd = hasEnd;
		this.hasNether= hasNether;
		this.rarity = rarity;
	}

	public int getMetadata() {
		return this.meta;
	}

	public int getColor() {
		return this.color.toInt();
	}

	public boolean hasGravel() {
		return this.hasGravel;
	}
	
	public boolean hasNether() {
		return this.hasNether;
	}
	
	public boolean hasEnd() {
		return this.hasEnd;
	}

	public int getRarity() {
		return this.rarity;
	}

	public static EnumOre fromMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	static {
		for (EnumOre ore : EnumOre.values()) {
			META_LOOKUP[ore.getMetadata()] = ore;
		}
	}

	public String getOreDictName(String pre) {
		String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);
		return pre + name1;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
