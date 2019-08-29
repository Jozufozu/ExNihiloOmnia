package com.jozufozu.exnihiloomnia.integration.jei.hammer

import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import java.util.*

class HammerRecipeWrapper(val recipe: HammerRecipe) : IRecipeWrapper {
    val rewards: Map<ItemStack, List<WeightedDrop>> = recipe.rewards.equivalencies


    override fun getIngredients(iIngredients: IIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, ArrayList<ItemStack>(recipe.ingredient.stacks))

        iIngredients.setOutputLists(VanillaTypes.ITEM, JeiPlugin.getRewardsOutput(rewards.keys, 9))
    }
}
