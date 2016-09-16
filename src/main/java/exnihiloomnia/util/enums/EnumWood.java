package exnihiloomnia.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumWood implements IStringSerializable {
	OAK(0, "oak"),
	SPRUCE(1, "spruce"),
	BIRCH(2, "birch"),
	JUNGLE(3, "jungle"),
	ACACIA(4, "acacia"),
	DARKOAK(5, "dark_oak");
	
	private final int meta;
	private final String name;
	
	private static final EnumWood[] META_LOOKUP = new EnumWood[values().length];
	
	EnumWood(int meta, String name) {
		this.meta = meta;
		this.name = name;
	}
	
	public int getMetadata() {
        return this.meta;
    }
	
	public static EnumWood fromMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
	
	static {
		EnumWood[] woods = values();

        for (int x = 0; x < woods.length; ++x) {
        	EnumWood wood = woods[x];
            META_LOOKUP[wood.getMetadata()] = wood;
        }
	}

	@Override
	public String getName() {
		return this.name;
	}
}
