package exnihiloomnia.compatibility.jei.categories;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.registries.sifting.SieveReward;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class JEISieveRecipe implements IRecipeWrapper{

    private ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
    private ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();

    private HashMap<ItemStack, ArrayList<SieveReward>> rewards = new HashMap<ItemStack, ArrayList<SieveReward>>();

    public JEISieveRecipe(SieveRegistryEntry entry) {

        for(SieveReward reward : entry.getRewards()) {

            if (!rewards.containsKey(reward.getItem()))
                rewards.put(reward.getItem(), new ArrayList<SieveReward>());
            rewards.get(reward.getItem()).add(reward);
        }

        outputs = Lists.newArrayList();

        for(ArrayList<SieveReward> r : rewards.values()) {
            ItemStack stack = r.get(0).getItem();
            if (!outputs.contains(stack))
                outputs.add(stack);
        }

        ItemStack inputStack = new ItemStack(entry.getInput().getBlock());
        inputs.add(inputStack);
    }

    Collection<SieveReward> getRewardFromItemStack(ItemStack stack) {
        return rewards.get(stack);
    }

    @Override
    public List getInputs() {
        return inputs;
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
