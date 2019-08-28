package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.registries.IForgeRegistryEntry

class SieveRecipe : IForgeRegistryEntry.Impl<SieveRecipe> {
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
    lateinit var output: WeightedRewards
        private set

    private constructor() {}

    constructor(input: Ingredient, siftTime: Int, output: WeightedRewards) {
        this.input = input
        this.siftTime = siftTime
        this.output = output
    }

    fun matches(input: ItemStack): Boolean {
        return this.input.apply(input)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): SieveRecipe {
            val recipe = SieveRecipe()

            if (!jsonObject.has(LibRegistries.INPUT_BLOCK)) {
                throw JsonSyntaxException("Sieve recipe has no input!")
            }

            val input = jsonObject.getAsJsonObject(LibRegistries.INPUT_BLOCK)

            recipe.input = JsonHelper.deserializeBlockIngredient(input)
            recipe.siftTime = JsonUtils.getInt(jsonObject, LibRegistries.TIME, 80)

            recipe.output = WeightedRewards.deserialize(jsonObject.getAsJsonArray(LibRegistries.REWARDS))

            return recipe
        }
    }
}
