package com.jozufozu.exnihiloomnia.integration.jei.sieve

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.jozufozu.exnihiloomnia.common.registries.WeightedDrop
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe
import com.jozufozu.exnihiloomnia.integration.jei.JeiPlugin
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.items.ItemHandlerHelper

class SieveRecipeWrapper(recipe: SieveRecipe) : IRecipeWrapper {
    val input: Ingredient = recipe.input
    val rewards: Map<ItemStack, Map<String, List<WeightedDrop>>>

    init {
        val rewards = Maps.newHashMap<ItemStack, HashMap<String, ArrayList<WeightedDrop>>>()

        for (reward in recipe.rewards.outputs) {
            val drop = reward.drop

            if (drop.isEmpty) continue

            val entry = rewards.entries.firstOrNull { drop.isItemEqual(it.key) }

            if (entry == null) {
                rewards[ItemHandlerHelper.copyStackWithSize(drop, 1)] = hashMapOf(Pair(reward.type, arrayListOf(reward)))
            } else {
                entry.value.getOrPut(reward.type, ::ArrayList).add(reward)
            }
        }

        this.rewards = rewards
    }

    override fun getIngredients(iIngredients: IIngredients) {
        val inputs = ArrayList<List<ItemStack>>()
        inputs.add(Lists.newArrayList(*input.matchingStacks))
        iIngredients.setInputLists(VanillaTypes.ITEM, inputs)

        iIngredients.setOutputLists(VanillaTypes.ITEM, JeiPlugin.getRewardsOutput(rewards.keys, 45))
    }
}
