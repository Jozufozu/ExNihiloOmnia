package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.registries.IForgeRegistryEntry

class CompostRecipe : IForgeRegistryEntry.Impl<CompostRecipe>() {
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
     * The color that the compost will appear as in the
     */
    lateinit var color: Color
        private set

    /**
     * The item that will be given once composting is complete
     */
    private var _output = ItemStack(Blocks.DIRT)

    val output: ItemStack
        get() = _output.copy()


    fun matches(stack: ItemStack): Boolean {
        return input.apply(stack)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(recipeObject: JsonObject): CompostRecipe {
            if (!recipeObject.has(LibRegistries.INPUT_GENERIC)) {
                throw JsonSyntaxException("Compost recipe has no input!")
            }

            val out = CompostRecipe()

            val input = recipeObject.getAsJsonObject(LibRegistries.INPUT_GENERIC)

            out.input = JsonHelper.deserializeIngredient(input)
            out.color = JsonHelper.deserializeColor(JsonUtils.getString(recipeObject, LibRegistries.COLOR, "ffffff"))

            out.amount = JsonUtils.getInt(input, LibRegistries.VOLUME, 125)

            if (recipeObject.has(LibRegistries.OUTPUT_GENERIC)) {
                out._output = JsonHelper.deserializeItem(recipeObject.getAsJsonObject(LibRegistries.OUTPUT_GENERIC), true)
            }

            return out
        }
    }
}
