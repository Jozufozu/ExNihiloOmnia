package exnihiloomnia.registries.hammering.pojos;

import java.util.ArrayList;

import exnihiloomnia.util.enums.EnumMetadataBehavior;

public class HammerRecipe {
	private String id;
	private int meta;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.IGNORED;
	private ArrayList<HammerRecipeReward> rewards = new ArrayList<HammerRecipeReward>();
	
	public HammerRecipe(){}
	public HammerRecipe(String id, int meta, EnumMetadataBehavior behavior) {
		this.id = id;
		this.meta = meta;
		this.behavior = behavior;
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
	
	public ArrayList<HammerRecipeReward> getRewards() {
		return rewards;
	}
	
	public void setRewards(ArrayList<HammerRecipeReward> rewards) {
		this.rewards = rewards;
	}
}
