package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import net.minecraft.block.BlockState
import net.minecraft.util.JSONUtils
import net.minecraftforge.registries.ForgeRegistryEntry

class HeatSource {
    var heatLevel: Int = 0
        private set

    lateinit var heatSource: WorldIngredient
        private set

    fun matches(state: BlockState): Boolean {
        return heatSource.test(state)
    }
}
