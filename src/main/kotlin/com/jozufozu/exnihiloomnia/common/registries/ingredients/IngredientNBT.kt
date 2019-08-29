package com.jozufozu.exnihiloomnia.common.registries.ingredients

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

/**
 * Forge's has a protected constructor
 */
class IngredientNBT(private val stack: ItemStack) : Ingredient(stack) {

    override fun apply(input: ItemStack?): Boolean {
        return if (input == null) false else ItemStack.areItemStacksEqualUsingNBTShareTag(this.stack, input)
    }
}
