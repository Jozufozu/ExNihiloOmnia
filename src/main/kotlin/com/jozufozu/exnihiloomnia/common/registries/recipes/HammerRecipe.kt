package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.state.IBlockState
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.IForgeRegistryEntry

class HammerRecipe(
        val ingredient: WorldIngredient,
        val rewards: WeightedRewards
) : IForgeRegistryEntry.Impl<HammerRecipe>() {

    fun matches(iBlockState: IBlockState): Boolean {
        return this.ingredient.test(iBlockState)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): HammerRecipe? {
            if (LibRegistries.WORLD_INPUT !in recipe) throw JsonSyntaxException("hammer recipe is missing input")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            val ingredient = WorldIngredient.deserialize(recipe[LibRegistries.WORLD_INPUT])

            RegistryLoader.pushCtx(LibRegistries.REWARDS)
            val rewards = WeightedRewards.deserialize(JsonUtils.getJsonArray(recipe, LibRegistries.REWARDS))
            RegistryLoader.popCtx()

            return HammerRecipe(ingredient, rewards)
        }
    }
}
