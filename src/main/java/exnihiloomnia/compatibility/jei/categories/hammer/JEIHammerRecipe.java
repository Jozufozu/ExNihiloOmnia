package exnihiloomnia.compatibility.jei.categories.hammer;

import com.google.common.collect.Lists;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.hammering.HammerReward;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class JEIHammerRecipe implements IRecipeWrapper{

    private final ArrayList<ItemStack> input = new ArrayList<>();
    private ArrayList<ItemStack> outputs = new ArrayList<>();
    private final HashMap<ItemStack, ArrayList<HammerReward>> rewards = new HashMap<>();
    private final HammerRegistryEntry entry;

    public JEIHammerRecipe(HammerRegistryEntry entry) {
        this.entry = entry;

        for(HammerReward reward : entry.getRewards()) {

            if (!rewards.containsKey(reward.getItem()))
                rewards.put(reward.getItem(), new ArrayList<>());
            rewards.get(reward.getItem()).add(reward);
        }

        outputs = Lists.newArrayList();

        for(ArrayList<HammerReward> r : rewards.values()) {
            ItemStack stack = r.get(0).getItem();
            if (!outputs.contains(stack))
                outputs.add(stack);
        }

        ItemStack inputStack = new ItemStack(entry.getInput().getBlock(), 1, entry.getInput().getBlock() != Blocks.FURNACE ? entry.getInput().getBlock().getMetaFromState(entry.getInput()) : 0);
        input.add(inputStack);
    }

    Collection<HammerReward> getRewardFromItemStack(ItemStack stack) {
        return rewards.get(stack);
    }

    public HammerRegistryEntry getEntry() {
        return entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public List getInputs() {
        return input;
    }

    @Override
    public List getOutputs() {
        return outputs;
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return null;
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
