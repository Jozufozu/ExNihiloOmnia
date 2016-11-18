
package exnihiloomnia.compatibility.jei.categories.hammer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import exnihiloomnia.ENO;
import exnihiloomnia.registries.hammering.HammerReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class HammerRecipeCategory implements IRecipeCategory<JEIHammerRecipe> {

    public static final String UID = "exnihiloomnia:hammer";
    private static final ResourceLocation texture = new ResourceLocation(ENO.MODID, "textures/gui/jei_generic.png");
    private static final ResourceLocation hammer = new ResourceLocation(ENO.MODID, "textures/items/hammer_diamond.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public HammerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 74);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei." + UID);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        minecraft.getTextureManager().bindTexture(hammer);
        Gui.drawScaledCustomSizeModalRect(75, 32, 0, 0, 16, 16, 16, 16, 16, 16);
        if(hasHighlight) {
            slotHighlight.draw(minecraft, highlightX, highlightY);
        }
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final JEIHammerRecipe recipeWrapper, IIngredients ingredients) {

        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

        IFocus<ItemStack> focus = recipeLayout.getItemStacks().getFocus();
        hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;
        final List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
        final int outputSlots = 1;
        int slotNumber = 0;

        for(ItemStack output : outputs) {
            final int slotX = 2 + (slotNumber % 9 * 18);
            final int slotY = 52 + (slotNumber / 9 * 18);

            recipeLayout.getItemStacks().init(outputSlots + slotNumber, false, slotX, slotY);

            recipeLayout.getItemStacks().set(outputSlots + slotNumber, output);
            ItemStack focusStack = focus.getValue();

            if(focus.getMode() == IFocus.Mode.OUTPUT && focusStack != null
                    && focusStack.getItem() == output.getItem()
                    && focusStack.getItemDamage() == output.getItemDamage()) {
                highlightX = slotX;
                highlightY = slotY;

            }
            slotNumber++;
        }
        recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
            @Override
            public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
                if(!input) {
                    Multiset<String> condensedTooltips = HashMultiset.create();

                    for(HammerReward reward : recipeWrapper.getRewardFromItemStack(ingredient)) {
                        String s;
                        int iChance = reward.getBaseChance();

                        if(iChance > 0)
                            s = String.format("%3d%%", (int) (reward.getBaseChance()));
                        else
                            s = String.format("%1.1f%%", (float) (reward.getBaseChance()));

                        condensedTooltips.add(s);
                    }

                    tooltip.add(I18n.format("jei.exnihiloomnia:sieve.dropChance"));

                    for(String line : condensedTooltips.elementSet()) {
                        tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
                    }
                }
                else {
                    String format = "exnihiloomnia.info.meta.";
                    format += recipeWrapper.getEntry().getMetadataBehavior() == EnumMetadataBehavior.IGNORED ? "ignored" : "specific";
                    tooltip.add(I18n.format(format));
                }
            }
        });
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final JEIHammerRecipe recipeWrapper) {

        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));

        IFocus<ItemStack> focus = recipeLayout.getItemStacks().getFocus();
        hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;
        final List outputs = recipeWrapper.getOutputs();
        final int outputSlots = 1;
        int slotNumber = 0;

        for(Object output : outputs) {
            final int slotX = 2 + (slotNumber % 9 * 18);
            final int slotY = 52 + (slotNumber / 9 * 18);

            recipeLayout.getItemStacks().init(outputSlots + slotNumber, false, slotX, slotY);


            ItemStack outputItemStack = (ItemStack) output;

            recipeLayout.getItemStacks().set(outputSlots + slotNumber, outputItemStack);
            ItemStack focusStack = focus.getValue();

            if(focus.getMode() == IFocus.Mode.OUTPUT && focusStack != null
                    && focusStack.getItem() == outputItemStack.getItem()
                    && focusStack.getItemDamage() == outputItemStack.getItemDamage()) {
                highlightX = slotX;
                highlightY = slotY;

            }
            slotNumber++;
        }
        recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
            @Override
            public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
                if(!input) {
                    Multiset<String> condensedTooltips = HashMultiset.create();

                    for(HammerReward reward : recipeWrapper.getRewardFromItemStack(ingredient)) {
                        String s;
                        int iChance = reward.getBaseChance();

                        if(iChance > 0)
                            s = String.format("%3d%%", (int) (reward.getBaseChance()));
                        else
                            s = String.format("%1.1f%%", (float) (reward.getBaseChance()));

                        condensedTooltips.add(s);
                    }

                    tooltip.add(I18n.format("jei.exnihiloomnia:sieve.dropChance"));

                    for(String line : condensedTooltips.elementSet()) {
                        tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
                    }
                }
            }
        });
    }
}
