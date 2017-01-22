package exnihiloomnia.registries.hammering.pojos;

import exnihiloomnia.util.enums.EnumMetadataBehavior;

import java.util.ArrayList;

public class HammerRecipe {
	private String id;
	private int meta;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.IGNORED;
	private ArrayList<HammerRecipeReward> rewards = new ArrayList<>();
	
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
