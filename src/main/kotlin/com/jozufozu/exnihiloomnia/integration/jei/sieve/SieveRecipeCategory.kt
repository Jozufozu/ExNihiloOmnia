package com.jozufozu.exnihiloomnia.integration.jei.sieve

import com.jozufozu.exnihiloomnia.ExNihilo
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

class SieveRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<SieveRecipeWrapper> {
    private val background: IDrawable = guiHelper.createDrawable(texture, 0, 0, 166, 146)
    private var highlight: IDrawable = guiHelper.createDrawable(texture, 166, 0, 18, 18)

    override fun getUid(): String {
        return UID
    }

    override fun getTitle(): String {
        return I18n.format("jei.exnihiloomnia.sieve")
    }

    override fun getModName(): String {
        return ExNihilo.NAME
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {

    }

    override fun setRecipe(recipeLayout: IRecipeLayout, sieveRecipeWrapper: SieveRecipeWrapper, ingredients: IIngredients) {
        val itemStacks = recipeLayout.itemStacks

        itemStacks.init(0, true, 74, 9)

        val outputSlots = 1

        for (slotNumber in 0..44) {
            val slotX = 2 + slotNumber % 9 * 18
            val slotY = 52 + slotNumber / 9 * 18

            itemStacks.init(outputSlots + slotNumber, false, slotX, slotY)
        }

        itemStacks.set(ingredients)

        itemStacks.addTooltipCallback { _, isInput, stack, toolTip ->
            if (isInput)
                return@addTooltipCallback

            sieveRecipeWrapper.rewards[stack]?.let {
                for ((type, dropList) in it) {
                    val translationKey = "exnihiloomnia.drops.type.$type"
                    val localizedTypeName = if (I18n.hasKey(translationKey)) I18n.format(translationKey) else type

                    toolTip.add(I18n.format("jei.exnihiloomnia.info.type_list", localizedTypeName))

                    for (weightedDrop in dropList) {
                        val percentChance = weightedDrop.chance * 100
                        val count = weightedDrop.drop.count

                        toolTip.add(I18n.format("jei.exnihiloomnia.info.reward_chance", percentChance, count))
                    }
                }
            }
        }
    }

    companion object {
        const val UID = "exnihiloomnia.sieve"

        private val texture = ResourceLocation(ExNihilo.MODID, "textures/gui/jei_sieve.png")
    }
}
