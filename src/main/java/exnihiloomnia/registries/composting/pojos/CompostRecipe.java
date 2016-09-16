package exnihiloomnia.registries.composting.pojos;

import exnihiloomnia.util.enums.EnumMetadataBehavior;

public class CompostRecipe {
	private String id;
	private int meta;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.IGNORED;
	private int value;
	private String color;
	
	public CompostRecipe() {}
	
	public CompostRecipe(String id, int meta, EnumMetadataBehavior behavior, int value, String color) {
		this.id = id;
		this.meta = meta;
		this.behavior = behavior;
		this.value = value;
		this.color = color;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getMeta() {
		return meta;
	}
	
	public void setMeta(int meta) {
		this.meta = meta;
	}
	
	public EnumMetadataBehavior getBehavior() {
		return behavior;
	}
	
	public void setBehavior(EnumMetadataBehavior behavior) {
		this.behavior = behavior;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}	
}
