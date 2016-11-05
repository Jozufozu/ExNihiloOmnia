package exnihiloomnia.registries.crook;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrookReward {
	private int base_chance;
	private int fortune_modifier; //the effectiveness of Fortune enchantments.
	private ItemStack item;
	
	public CrookReward(ItemStack item, int base_chance, int fortune_modifier) {
		this.item = item;
		this.base_chance = base_chance;
		this.fortune_modifier = fortune_modifier;
	}

	public void dropReward(EntityPlayer player, BlockPos pos) {
		ItemStack reward = getReward(player);
		if (reward != null)
			Block.spawnAsEntity(player.worldObj, pos, reward);
	}

	public ItemStack getReward(EntityPlayer player) {
		World world = player.worldObj;
		int luck_level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getActiveItemStack());
		int chance = base_chance + (fortune_modifier * luck_level);

		if (world.rand.nextInt(100) <= chance) {
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

	public int getFortuneModifier() {
		return fortune_modifier;
	}

	public void setFortuneModifier(int luck_modifier) {
		this.fortune_modifier = luck_modifier;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
}
