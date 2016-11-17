
package exnihiloomnia.compatibility.jei.categories.compost;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import exnihiloomnia.util.Color;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompostRecipeCategory implements IRecipeCategory<JEICompostRecipe> {

    public static final String UID = "exnihiloomnia:compost";
    private static final ResourceLocation texture = new ResourceLocation(ENO.MODID, "textures/gui/jei_compost.png");
    private static ResourceLocation compost = new ResourceLocation("exnihiloomnia:textures/blocks/compost.png");

    private CompostRegistryEntry entry;

    private final IDrawableStatic background;

    public CompostRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 128, 64);
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
        minecraft.getTextureManager().bindTexture(compost);
        drawRect(57, 24, 14, 10, entry.getColor());
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {
    }

    private void drawRect(int x, int y, int width, int height, Color color) {
        VertexBuffer buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(x        , y + height, 0).tex(0.0, 1.0).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x + width, y + height, 0).tex(1.0, 1.0).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x + width, y         , 0).tex(1.0, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x        , y         , 0).tex(0.0, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
        Tessellator.getInstance().draw();
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final JEICompostRecipe recipeWrapper) {
        this.entry = recipeWrapper.getEntry();

        recipeLayout.getItemStacks().init(0, true, 23, 23);
        recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));

        recipeLayout.getItemStacks().init(1, true, 87, 23);
        recipeLayout.getItemStacks().set(1, (ItemStack) recipeWrapper.getOutputs().get(0));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, JEICompostRecipe recipeWrapper, IIngredients ingredients) {
        this.entry = recipeWrapper.getEntry();

        recipeLayout.getItemStacks().init(0, true, 23, 23);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

        recipeLayout.getItemStacks().init(1, true, 87, 23);
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
