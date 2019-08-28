package com.jozufozu.exnihiloomnia.integration.jei.hammer

import com.google.common.collect.Lists
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IIngredientType
import mezz.jei.api.recipe.IRecipeWrapper
import mezz.jei.ingredients.Ingredients
import net.minecraft.item.ItemStack

import java.util.ArrayList

class HammerRecipeWrapper(val recipe: HammerRecipe) : IRecipeWrapper {
    val rewards: Map<ItemStack, List<WeightedDrop>> = recipe.rewards.equivalencies


    override fun getIngredients(iIngredients: IIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, ArrayList<ItemStack>(recipe.ingredient.stacks))

        iIngredients.setOutputLists(VanillaTypes.ITEM, JeiPlugin.getRewardsOutput(rewards.keys, 9))
    }
}
