package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.registries.WorldIngredient
import net.minecraft.block.BlockState
import net.minecraft.util.Identifier

class HammerRecipe {
    lateinit var ingredient: WorldIngredient
        private set

    lateinit var rewards: Identifier
        private set

    fun matches(iBlockState: BlockState): Boolean {
        return this.ingredient.test(iBlockState)
    }
}
