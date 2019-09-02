package com.jozufozu.exnihiloomnia.integration.jei.melting

import com.google.common.collect.Lists
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import java.util.*

class MeltingRecipeWrapper(val recipe: MeltingRecipe) : IRecipeWrapper {

    override fun getIngredients(iIngredients: IIngredients) {
        val inputs = ArrayList<List<ItemStack>>()
        inputs.add(Lists.newArrayList(*recipe.input.matchingStacks))
        iIngredients.setInputLists(VanillaTypes.ITEM, inputs)

        iIngredients.setOutput(VanillaTypes.FLUID, recipe.output)
    }
}
