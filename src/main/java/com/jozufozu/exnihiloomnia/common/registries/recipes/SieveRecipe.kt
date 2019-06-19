package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation

class SieveRecipe {
    /**
     * What you have to put in the sieve
     */
    lateinit var input: Ingredient
        private set

    /**
     * The amount of time in ticks it takes to sift the input.
     * Default is 40 ticks, or 2 seconds
     */
    var siftTime = 40
        private set

    /**
     * What could drop when fully processed.
     */
    lateinit var loot: ResourceLocation
        private set

    fun matches(input: ItemStack): Boolean {
        return this.input.test(input)
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(json: JsonObject): SieveRecipe {
            val recipe = SieveRecipe()

            if (!json.has(RegistriesLib.INPUT_BLOCK)) {
                throw JsonSyntaxException("Sieve recipe has no input!")
            }

            val input = json.getAsJsonObject(RegistriesLib.INPUT_BLOCK)

            recipe.input = JsonHelper.deserializeBlockIngredient(input)
            recipe.siftTime = JSONUtils.getInt(json, RegistriesLib.TIME, recipe.siftTime)

            recipe.loot = ResourceLocation(JSONUtils.getString(json, RegistriesLib.REWARDS))

            return recipe
        }
    }
}
