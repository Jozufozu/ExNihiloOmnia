package exnihiloomnia.registries.hammering.pojos;

public class HammerRecipeReward {
	private String id;
	private int meta;
	private int amount;
	private int baseChance = 0;
	private int fortuneModifier = 0;
	
	public HammerRecipeReward(){}
	
	public HammerRecipeReward(String id, int meta, int amount, int baseChance, int fortuneModifier) {
		this.id = id;
		this.meta = meta;
		this.amount = amount;
		this.baseChance = baseChance;
		this.fortuneModifier = fortuneModifier;
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
	
	public int getBaseChance() {
		return baseChance;
	}
	
	public void setBaseChance(int baseChance) {
		this.baseChance = baseChance;
	}
	
	public int getFortuneModifier() {
		return fortuneModifier;
	}
	
	public void setFortuneModifier(int fortuneModifier) {
		this.fortuneModifier = fortuneModifier;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
