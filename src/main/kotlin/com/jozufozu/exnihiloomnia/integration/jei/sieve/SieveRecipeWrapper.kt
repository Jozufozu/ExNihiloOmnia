package com.jozufozu.exnihiloomnia.integration.jei.sieve

import com.google.common.collect.Lists
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import java.util.*

class SieveRecipeWrapper(recipe: SieveRecipe) : IRecipeWrapper {
    val input: Ingredient = recipe.input
    val rewards: Map<ItemStack, List<WeightedDrop>> = recipe.output.equivalencies

    override fun getIngredients(iIngredients: IIngredients) {
        val inputs = ArrayList<List<ItemStack>>()
        inputs.add(Lists.newArrayList(*input.matchingStacks))
        iIngredients.setInputLists(VanillaTypes.ITEM, inputs)

        iIngredients.setOutputLists(VanillaTypes.ITEM, JeiPlugin.getRewardsOutput(rewards.keys, 45))
    }
}
