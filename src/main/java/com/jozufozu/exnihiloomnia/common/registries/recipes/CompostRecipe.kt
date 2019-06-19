package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JSONUtils

class CompostRecipe {
    /**
     * The thing that you put in to compost
     */
    lateinit var input: Ingredient
        private set

    /**
     * How much stuff each item will fill the barrel with, in mB
     * Default is 125, or 8 items to fill the barrel
     */
    var amount = 125
        private set

    /**
     * The item that will be given once composting is complete
     */
    var output = ItemStack(Blocks.DIRT)
        get() = field.copy()
        private set

    /**
     * The color that the compost will appear as in the
     */
    lateinit var color: Color
        private set

    fun matches(stack: ItemStack): Boolean {
        return input.test(stack)
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(recipeObject: JsonObject): CompostRecipe {
            if (!recipeObject.has(RegistriesLib.INPUT)) {
                throw JsonSyntaxException("Compost recipe has no input!")
            }

            val out = CompostRecipe()

            val input = recipeObject.getAsJsonObject(RegistriesLib.INPUT)

            out.input = JsonHelper.deserializeIngredient(input)
            out.color = Color.deserialize(JSONUtils.getString(recipeObject, RegistriesLib.COLOR, "ffffff"))

            out.amount = JSONUtils.getInt(input, RegistriesLib.VOLUME, 125)

            if (recipeObject.has(RegistriesLib.OUTPUT)) {
                out.output = JsonHelper.deserializeItem(recipeObject.getAsJsonObject(RegistriesLib.OUTPUT), true)
            }

            return out
        }
    }
}
