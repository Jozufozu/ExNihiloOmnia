package exnihiloomnia.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumOreBlockType implements IStringSerializable{
	GRAVEL("gravel", EnumOreItemType.BROKEN),
	GRAVEL_NETHER("gravel_nether", EnumOreItemType.BROKEN_NETHER),
	GRAVEL_ENDER("gravel_ender", EnumOreItemType.BROKEN_ENDER),
	SAND("sand", EnumOreItemType.CRUSHED),
	DUST("dust", EnumOreItemType.POWDERED);

	private final String name;
	private final EnumOreItemType co;

	EnumOreBlockType(String name, EnumOreItemType co)
	{
		this.name = name;
		this.co = co;
	}

	@Override
	public String getName() {
		return this.name;
	}
	public EnumOreItemType getCo() {
		return this.co;
	}

}
