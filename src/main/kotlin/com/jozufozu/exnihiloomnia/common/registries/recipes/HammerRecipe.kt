package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import net.minecraft.block.state.IBlockState
import net.minecraftforge.registries.IForgeRegistryEntry

class HammerRecipe : IForgeRegistryEntry.Impl<HammerRecipe>() {
    lateinit var ingredient: WorldIngredient
        private set
    lateinit var rewards: WeightedRewards
        private set

    fun matches(iBlockState: IBlockState): Boolean {
        return this.ingredient.test(iBlockState)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): HammerRecipe {
            val out = HammerRecipe()

            if (!jsonObject.has(LibRegistries.INPUT_BLOCK)) {
                throw JsonSyntaxException("Hammer recipe is missing input!")
            }

            out.ingredient = WorldIngredient.deserialize(jsonObject.getAsJsonObject(LibRegistries.INPUT_BLOCK))

            out.rewards = WeightedRewards.deserialize(jsonObject.getAsJsonArray(LibRegistries.REWARDS))

            return out
        }
    }
}
