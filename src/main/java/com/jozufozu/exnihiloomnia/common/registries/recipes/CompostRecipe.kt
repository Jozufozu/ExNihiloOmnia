package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.StringNbtReader
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper

data class CompostRecipe(override val identifier: Identifier,
                         val input: Ingredient,
                         val amount: Int = 125,
                         val color: Color,
                         val output: () -> ItemStack = { ItemStack(Blocks.DIRT) }): Identifiable<CompostRecipe> {
    fun matches(stack: ItemStack): Boolean {
        return input.test(stack)
    }
}
