package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.JSONUtils
import net.minecraft.util.JsonHelper
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
    lateinit var loot: Identifier
        private set

    fun matches(input: ItemStack): Boolean {
        return this.input.test(input)
    }
}
