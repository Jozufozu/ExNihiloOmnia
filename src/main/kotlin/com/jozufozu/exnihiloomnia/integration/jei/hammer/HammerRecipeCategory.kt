package com.jozufozu.exnihiloomnia.integration.jei.hammer

import com.jozufozu.exnihiloomnia.ExNihilo
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

class HammerRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<HammerRecipeWrapper> {

    private val background: IDrawable = guiHelper.createDrawable(texture, 0, 0, 166, 74)
    private var highlight: IDrawable = guiHelper.createDrawable(texture, 166, 0, 18, 18)

    override fun getUid(): String {
        return UID
    }

    override fun getTitle(): String {
        return I18n.format("jei.exnihiloomnia.hammer")
    }

    override fun getModName(): String {
        return ExNihilo.NAME
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, hammerRecipeWrapper: HammerRecipeWrapper, ingredients: IIngredients) {
        val itemStacks = recipeLayout.itemStacks

        itemStacks.init(0, true, 74, 9)

        val outputSlots = 1

        for (slotNumber in 0..8) {
            val slotX = 2 + slotNumber % 9 * 18
            val slotY = 52 + slotNumber / 9 * 18

            itemStacks.init(outputSlots + slotNumber, false, slotX, slotY)
        }

        itemStacks.set(ingredients)

        itemStacks.addTooltipCallback { slot, isInput, stack, toolTip ->
            if (isInput)
                return@addTooltipCallback

            hammerRecipeWrapper.rewards[stack]?.let {
                toolTip.add("")
                for (weightedDrop in it) {
                    val percentChance = weightedDrop.chance * 100
                    val count = weightedDrop.drop.count

                    val drop = I18n.format("jei.exnihiloomnia.info.reward_chance", percentChance, count)

                    toolTip.add(drop)
                }
            }
        }
    }

    companion object {
        const val UID = "exnihiloomnia.hammer"

        private val texture = ResourceLocation(ExNihilo.MODID, "textures/gui/jei_hammer.png")
    }
}
