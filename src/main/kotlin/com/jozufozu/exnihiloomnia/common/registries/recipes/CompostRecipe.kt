package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.IForgeRegistryEntry

class CompostRecipe(
        /**
         * The thing that you put in to compost
         */
        val input: Ingredient,

        /**
         * The color that the compost will appear as in the
         */
        val color: Color,

        /**
         * How much stuff each item will fill the barrel with, in mB
         * Default is 125, or 8 items to fill the barrel
         */
        val volume: Int = 125,

        /**
         * The item that will be given once composting is complete
         */
        output: ItemStack = ItemStack(Blocks.DIRT)
) : IForgeRegistryEntry.Impl<CompostRecipe>() {

    val output: ItemStack = output
        get() = field.copy()


    fun matches(stack: ItemStack) = input.apply(stack)

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): CompostRecipe? {
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("compost recipe has no input")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            RegistryLoader.pushCtx(LibRegistries.INPUT)
            val input = CraftingHelper.getIngredient(recipe[LibRegistries.INPUT], RegistryLoader.CONTEXT)
            RegistryLoader.popCtx()

            val color = JsonHelper.deserializeColor(JsonUtils.getString(recipe, LibRegistries.COLOR, "ffffff"))
            val volume = JsonUtils.getInt(recipe, LibRegistries.VOLUME, 125)

            return if (LibRegistries.OUTPUT in recipe) {
                val output = JsonHelper.deserializeItem(JsonUtils.getJsonObject(recipe, LibRegistries.OUTPUT), true)
                CompostRecipe(input, color, volume, output)
            } else {
                CompostRecipe(input, color, volume)
            }
        }
    }
}
