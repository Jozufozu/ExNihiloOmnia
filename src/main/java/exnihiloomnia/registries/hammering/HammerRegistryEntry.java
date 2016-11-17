package exnihiloomnia.registries.hammering;

import exnihiloomnia.registries.hammering.pojos.HammerRecipe;
import exnihiloomnia.registries.hammering.pojos.HammerRecipeReward;
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

public class HammerRegistryEntry {
	private IBlockState input;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.SPECIFIC;
	private ArrayList<HammerReward> rewards = new ArrayList<HammerReward>();

	public HammerRegistryEntry(IBlockState input, EnumMetadataBehavior behavior) {
		this.input = input;
		this.behavior = behavior;
	}

	public IBlockState getInput() {
		return input;
	}

	public void addReward(ItemStack item, int base_chance, int luck_modifier) {
		this.rewards.add(new HammerReward(item, base_chance, luck_modifier));
	}

	public ArrayList<HammerReward> getRewards() {
		return rewards;
	}

	public EnumMetadataBehavior getMetadataBehavior() {
		return this.behavior;
	}

	public void dropRewards(EntityPlayer player, BlockPos pos) {
		for (HammerReward reward : rewards) {
			reward.dropReward(player, pos);
		}
	}

	public List<ItemStack> rollRewards(EntityPlayer player) {
		ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();

		for (HammerReward reward : this.rewards) {
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

	public static HammerRegistryEntry fromRecipe(HammerRecipe recipe) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getId()));

		if (block != null) {
			IBlockState state = block.getBlockState().getBaseState();

			if (state != null) {
				HammerRegistryEntry entry = new HammerRegistryEntry(state, recipe.getBehavior());

				for (HammerRecipeReward reward : recipe.getRewards()) {
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

	public HammerRecipe toRecipe() {
		String block = Block.REGISTRY.getNameForObject(this.getInput().getBlock()).toString();

		HammerRecipe recipe = new HammerRecipe(block, this.getInput().getBlock().getMetaFromState(this.getInput()), this.getMetadataBehavior());

		ArrayList<HammerRecipeReward> rewards = new ArrayList<HammerRecipeReward>();

		for (HammerReward reward : this.getRewards())
			rewards.add(new HammerRecipeReward(Item.REGISTRY.getNameForObject(reward.getItem().getItem()).toString(), reward.getItem().getMetadata(), reward.getItem().stackSize, reward.getBaseChance(), reward.getFortuneModifier()));

		recipe.setRewards(rewards);

		return recipe;
	}
}
