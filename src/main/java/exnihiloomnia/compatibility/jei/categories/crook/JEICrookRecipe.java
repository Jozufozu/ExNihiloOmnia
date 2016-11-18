package exnihiloomnia.compatibility.jei.categories.crook;

import com.google.common.collect.Lists;
import exnihiloomnia.registries.crook.CrookRegistryEntry;
import exnihiloomnia.registries.crook.CrookReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class JEICrookRecipe implements IRecipeWrapper{

    private ItemStack input;
    private ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
    private HashMap<ItemStack, ArrayList<CrookReward>> rewards = new HashMap<ItemStack, ArrayList<CrookReward>>();
    private CrookRegistryEntry entry;

    public JEICrookRecipe(CrookRegistryEntry entry) {
        this.entry = entry;

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

        int meta = entry.getMetadataBehavior() == EnumMetadataBehavior.IGNORED ? 0 : entry.getInput().getBlock().getMetaFromState(entry.getInput());
        input = new ItemStack(entry.getInput().getBlock(), 1, meta);
    }

    Collection<CrookReward> getRewardFromItemStack(ItemStack stack) {
        return rewards.get(stack);
    }

    public CrookRegistryEntry getEntry() {
        return entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public List getInputs() {
        return Collections.singletonList(input);
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
