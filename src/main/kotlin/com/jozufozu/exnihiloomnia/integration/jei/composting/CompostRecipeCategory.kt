package com.jozufozu.exnihiloomnia.integration.jei.composting

import com.jozufozu.exnihiloomnia.ExNihilo
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

class CompostRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<CompostRecipeWrapper> {

    init {
        CompostRecipeCategory.background = guiHelper.createDrawable(texture, 0, 0, 90, 39)
        overlay = guiHelper.createDrawable(texture, 90, 0, 18, 20)
        compost = guiHelper.createDrawable(compostLoc, 0, 0, 16, 16)
    }


    override fun getUid(): String {
        return UID
    }

    override fun getTitle(): String {
        return I18n.format("jei.exnihiloomnia.compost")
    }

    override fun getModName(): String {
        return ExNihilo.NAME
    }

    override fun getBackground(): IDrawable {
        return CompostRecipeCategory.background
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, compostRecipeWrapper: CompostRecipeWrapper, ingredients: IIngredients) {
        val itemStacks = recipeLayout.itemStacks

        itemStacks.init(0, true, 4, 15)

        itemStacks.init(1, false, 68, 15)

        itemStacks.set(ingredients)
    }

    companion object {
        const val UID = "exnihiloomnia.compost"

        val compostLoc = ResourceLocation(ExNihilo.MODID, "textures/blocks/compost.png")
        val texture = ResourceLocation(ExNihilo.MODID, "textures/gui/jei_barrel.png")

        lateinit var background: IDrawable
            private set
        lateinit var overlay: IDrawable
            private set
        lateinit var compost: IDrawable
            private set
    }
}
