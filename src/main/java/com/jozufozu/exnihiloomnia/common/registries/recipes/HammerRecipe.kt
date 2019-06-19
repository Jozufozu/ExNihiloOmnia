package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import net.minecraft.block.BlockState
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation

class HammerRecipe {
    lateinit var ingredient: WorldIngredient
        private set

    lateinit var rewards: ResourceLocation
        private set

    fun matches(iBlockState: BlockState): Boolean {
        return this.ingredient.test(iBlockState)
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(json: JsonObject): HammerRecipe {
            val out = HammerRecipe()

            if (!json.has(RegistriesLib.INPUT_BLOCK)) {
                throw JsonSyntaxException("Hammer recipe is missing input!")
            }

            out.ingredient = WorldIngredient.deserialize(json.getAsJsonObject(RegistriesLib.INPUT_BLOCK))

            out.rewards = ResourceLocation(JSONUtils.getString(json, RegistriesLib.REWARDS))

            return out
        }
    }
}
