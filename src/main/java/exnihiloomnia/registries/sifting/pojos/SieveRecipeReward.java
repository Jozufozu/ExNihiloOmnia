package exnihiloomnia.registries.sifting.pojos;

public class SieveRecipeReward {
	private String id;
	private int meta;
	private int amount;
	private int baseChance = 0;
	
	public SieveRecipeReward(){}
	
	public SieveRecipeReward(String id, int meta, int amount, int baseChance) {
		this.id = id;
		this.meta = meta;
		this.amount = amount;
		this.baseChance = baseChance;
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
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
