package exnihiloomnia.registries.sifting;

import exnihiloomnia.registries.sifting.pojos.SieveRecipe;
import exnihiloomnia.registries.sifting.pojos.SieveRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class SieveRegistryEntry {
	private IBlockState input;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.SPECIFIC;
	private ArrayList<SieveReward> rewards = new ArrayList<SieveReward>();
	
	public SieveRegistryEntry(IBlockState input, EnumMetadataBehavior behavior) {
		this.input = input;
		this.behavior = behavior;
	}
	
	public IBlockState getInput() {
		return input;
	}
	
	public void addReward(ItemStack item, int base_chance) {
		this.rewards.add(new SieveReward(item, base_chance));
	}
	
	public ArrayList<SieveReward> getRewards() {
		return rewards;
	}
	
	public EnumMetadataBehavior getMetadataBehavior() {
		return this.behavior;
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
	
	public static SieveRegistryEntry fromRecipe(SieveRecipe recipe) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getId()));

		if (block != null) {
			IBlockState state = recipe.getBehavior() == EnumMetadataBehavior.SPECIFIC ? block.getStateFromMeta(recipe.getMeta()) : block.getDefaultState();

			if (state != null) {
				SieveRegistryEntry entry = new SieveRegistryEntry(state, recipe.getBehavior());

				for (SieveRecipeReward reward : recipe.getRewards()) {
					Item item = Item.REGISTRY.getObject(new ResourceLocation(reward.getId()));

					if (item != null) {
						entry.addReward(new ItemStack(item, reward.getAmount(), reward.getMeta()), reward.getBaseChance());
					}
				}
				
				return entry;
			}
		}

		return null;
	}

	public SieveRecipe toRecipe() {
		String block = Block.REGISTRY.getNameForObject(this.getInput().getBlock()).toString();

		SieveRecipe recipe = new SieveRecipe(block, this.getInput().getBlock().getMetaFromState(this.getInput()), this.getMetadataBehavior());

		ArrayList<SieveRecipeReward> rewards = new ArrayList<SieveRecipeReward>();

		for (SieveReward reward : this.getRewards())
			rewards.add(new SieveRecipeReward(Item.REGISTRY.getNameForObject(reward.getItem().getItem()).toString(), reward.getItem().getMetadata(), reward.getItem().stackSize, reward.getBaseChance()));

		recipe.setRewards(rewards);

		return recipe;
	}
}
