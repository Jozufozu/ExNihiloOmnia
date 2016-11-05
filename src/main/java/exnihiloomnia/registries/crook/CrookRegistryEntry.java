package exnihiloomnia.registries.crook;

import exnihiloomnia.registries.crook.pojos.CrookRecipe;
import exnihiloomnia.registries.crook.pojos.CrookRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class CrookRegistryEntry {
	private IBlockState input;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.SPECIFIC;
	private ArrayList<CrookReward> rewards = new ArrayList<CrookReward>();

	public CrookRegistryEntry(IBlockState input, EnumMetadataBehavior behavior) {
		this.input = input;
		this.behavior = behavior;
	}

	public IBlockState getInput() {
		return input;
	}

	public void addReward(ItemStack item, int base_chance, int luck_modifier) {
		this.rewards.add(new CrookReward(item, base_chance, luck_modifier));
	}

	public ArrayList<CrookReward> getRewards() {
		return rewards;
	}

	public EnumMetadataBehavior getMetadataBehavior() {
		return this.behavior;
	}

	public void dropRewards(EntityPlayer player, BlockPos pos) {
		for (CrookReward reward : rewards) {
			reward.dropReward(player, pos);
		}
	}

	public List<ItemStack> rollRewards(EntityPlayer player) {
		ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();

		for (CrookReward reward : this.rewards) {
			ItemStack itemReward = reward.getReward(player);
			if (itemReward != null) {
				rewards.add(itemReward);
			}
		}

		return rewards;
	}

	public String getKey() {
		String s = Block.REGISTRY.getNameForObject(input.getBlock()).toString();

		if (behavior == EnumMetadataBehavior.IGNORED) {
			return s + ":*";
		}
		else {
			return s + ":" + input.getBlock().getMetaFromState(input);
		}
	}

	public static CrookRegistryEntry fromRecipe(CrookRecipe recipe) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getId()));

		if (block != null) {
			IBlockState state = block.getBlockState().getBaseState();

			if (state != null) {
				CrookRegistryEntry entry = new CrookRegistryEntry(state, recipe.getBehavior());

				for (CrookRecipeReward reward : recipe.getRewards()) {
					Item item = Item.REGISTRY.getObject(new ResourceLocation(reward.getId()));

					if (item != null) {
						entry.addReward(new ItemStack(item, reward.getAmount(), reward.getMeta()), reward.getBaseChance(), reward.getFortuneModifier());
					}
				}

				return entry;
			}
		}

		return null;
	}
}
