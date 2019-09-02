package com.jozufozu.exnihiloomnia.integration.jei.hammer

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemHandlerHelper

class HammerRecipeWrapper(val recipe: HammerRecipe) : IRecipeWrapper {
    val rewards: Map<ItemStack, List<WeightedDrop>>

    init {
        val rewards = Maps.newHashMap<ItemStack, ArrayList<WeightedDrop>>()

        for (reward in recipe.rewards.outputs) {
            val drop = reward.drop

            if (drop.isEmpty) continue

            val entry = rewards.entries.firstOrNull { drop.isItemEqual(it.key) }

            if (entry == null) {
                rewards[ItemHandlerHelper.copyStackWithSize(drop, 1)] = Lists.newArrayList(reward)
            } else {
                entry.value.add(reward)
            }
        }

        this.rewards = rewards
    }


    override fun getIngredients(iIngredients: IIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, ArrayList<ItemStack>(recipe.ingredient.stacks))

        iIngredients.setOutputLists(VanillaTypes.ITEM, JeiPlugin.getRewardsOutput(rewards.keys, 9))
    }
}
