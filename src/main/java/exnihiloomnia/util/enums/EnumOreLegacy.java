package exnihiloomnia.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumOreLegacy implements IStringSerializable {
	IRON("iron"),
	GOLD("gold"),
	TIN("tin"),
	COPPER("copper"),
	LEAD("lead"),
	SILVER("silver"),
	NICKEL("nickel"),
	PLATINUM("platinum"),
	ALUMINUM("aluminum"),
	OSMIUM("osmium"),
	ARDITE("ardite"),
	COBALT("cobalt"),
	DRACONIUM("draconium"),
	YELLORITE("yellorite"),
	URANIUM("uranium");

	private final String name;

	EnumOreLegacy(String name) {
		this.name = name;
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
