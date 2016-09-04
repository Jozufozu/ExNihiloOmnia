package exnihiloomnia.registries.hammering;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HammerReward
{
	private int base_chance;
	private int fortune_modifier; //the effectiveness of Fortune enchantments.
	private ItemStack item;
	
	public HammerReward(ItemStack item, int base_chance, int fortune_modifier)
	{
		this.item = item;
		this.base_chance = base_chance;
		this.fortune_modifier = fortune_modifier;
	}

	public void dropReward(EntityPlayer player, BlockPos pos)
	{
		World world = player.worldObj;
		int luck_level = getFortuneModifier();
		int chance = base_chance + (fortune_modifier * luck_level);

		if (world.rand.nextInt(100) < chance)
		{
			EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, item.copy());

			entityitem.motionX = world.rand.nextGaussian() * 0.05F;
			entityitem.motionY = (0.2d);
			entityitem.motionZ = world.rand.nextGaussian() * 0.05F;
			entityitem.setDefaultPickupDelay();

			world.spawnEntityInWorld(entityitem);
		}
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
