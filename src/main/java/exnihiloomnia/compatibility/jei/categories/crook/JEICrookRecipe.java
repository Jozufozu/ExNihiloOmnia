package exnihiloomnia.compatibility.jei.categories.crook;

import com.google.common.collect.Lists;
import exnihiloomnia.registries.crook.CrookRegistryEntry;
import exnihiloomnia.registries.crook.CrookReward;
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

public class JEICrookRecipe implements IRecipeWrapper{

    private ArrayList<ItemStack> input = new ArrayList<ItemStack>();
    private ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
    private HashMap<ItemStack, ArrayList<CrookReward>> rewards = new HashMap<ItemStack, ArrayList<CrookReward>>();


    public JEICrookRecipe(CrookRegistryEntry entry) {
        for(CrookReward reward : entry.getRewards()) {

            if (!rewards.containsKey(reward.getItem()))
                rewards.put(reward.getItem(), new ArrayList<CrookReward>());
            rewards.get(reward.getItem()).add(reward);
        }

        outputs = Lists.newArrayList();

        for(ArrayList<CrookReward> r : rewards.values()) {
            ItemStack stack = r.get(0).getItem();
            if (!outputs.contains(stack))
                outputs.add(stack);
        }

        ItemStack inputStack = new ItemStack(entry.getInput().getBlock(), 1, entry.getInput().getBlock() != Blocks.FURNACE ? entry.getInput().getBlock().getMetaFromState(entry.getInput()) : 0);
        input.add(inputStack);
    }

    Collection<CrookReward> getRewardFromItemStack(ItemStack stack) {
        return rewards.get(stack);
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
