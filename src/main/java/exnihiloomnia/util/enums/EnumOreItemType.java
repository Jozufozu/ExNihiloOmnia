package exnihiloomnia.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumOreItemType implements IStringSerializable{
	BROKEN("broken"),
	BROKEN_NETHER("broken_nether"),
	BROKEN_ENDER("broken_ender"),
	CRUSHED("crushed"),
	POWDERED("powder"),
	INGOT("ingot");

	private final String name;

	EnumOreItemType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
