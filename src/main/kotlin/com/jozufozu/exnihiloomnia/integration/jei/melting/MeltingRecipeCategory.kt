package com.jozufozu.exnihiloomnia.integration.jei.melting

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

class MeltingRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<MeltingRecipeWrapper> {
    private val background: IDrawable = guiHelper.createDrawable(texture, 0, 0, 40, 66)

    override fun getUid(): String {
        return UID
    }

    override fun getTitle(): String {
        return I18n.format("jei.exnihiloomnia.melting")
    }

    override fun getModName(): String {
        return ExNihilo.NAME
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft?) {

    }

    override fun setRecipe(recipeLayout: IRecipeLayout, meltingRecipeWrapper: MeltingRecipeWrapper, ingredients: IIngredients) {
        val itemStacks = recipeLayout.itemStacks
        val fluidStacks = recipeLayout.fluidStacks

        itemStacks.init(0, true, 11, 4)
        fluidStacks.init(0, false, 8, 30, 24, 26, ModConfig.blocks.crucible.fluidCapacity, true, null)

        itemStacks.set(ingredients)
        fluidStacks.set(ingredients)

        fluidStacks.addTooltipCallback { slotIndex, input, ingredient, tooltip ->
            tooltip.add("")
            tooltip.add(I18n.format("jei.exnihiloomnia.info.required_heat", meltingRecipeWrapper.recipe.requiredHeat))
        }
    }

    companion object {
        const val UID = "exnihiloomnia.melting"

        private val texture = ResourceLocation(ExNihilo.MODID, "textures/gui/jei_crucible.png")
    }
}
