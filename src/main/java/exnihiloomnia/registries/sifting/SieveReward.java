package exnihiloomnia.registries.sifting;

import java.util.Random;

import net.minecraft.item.ItemStack;

public class SieveReward {
	private static Random rand = new Random();
	
	private int base_chance;
	private ItemStack item;
	
	public SieveReward(ItemStack item, int base_chance) {
		this.item = item;
		this.base_chance = base_chance;
	}
	
	public ItemStack generateReward() {
		if (rand.nextInt(100) < base_chance) {
			return item.copy();
		}
		
		return null;
	}

	public int getBaseChance() {
		return base_chance;
	}

	public void setBaseChance(int base_chance) {
		this.base_chance = base_chance;
	}

	public ItemStack getItem() {
		return item;
	}

	public String getName() {
		return item.getUnlocalizedName() + ":" + item.getMetadata();
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
}
