package com.jozufozu.exnihiloomnia.integration.jei.composting

import com.google.common.collect.Lists
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import java.util.*

class CompostRecipeWrapper(val recipe: CompostRecipe) : IRecipeWrapper {

    override fun getIngredients(iIngredients: IIngredients) {
        val inputs = ArrayList<List<ItemStack>>()
        inputs.add(Lists.newArrayList(*recipe.input.matchingStacks))
        iIngredients.setInputLists(ItemStack::class.java, inputs)

        iIngredients.setOutput(ItemStack::class.java, recipe.output)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()

        val color = recipe.color
        GlStateManager.color(color.r, color.g, color.b, color.a)

        GlStateManager.translate(37f, 16f, 0f)
        GlStateManager.scale(1.0, 1.125, 1.0)

        CompostRecipeCategory.compost.draw(minecraft)

        GlStateManager.popMatrix()

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        CompostRecipeCategory.overlay.draw(minecraft, 36, 15)
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String> {
        return if (mouseX > 36 && mouseY > 15 && mouseX < 54 && mouseY < 35) {
            Lists.newArrayList(I18n.format("jei.exnihiloomnia.info.compost_amount", recipe.amount, ModConfig.blocks.barrel.compostCapacity))
        } else emptyList()

    }
}
