package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import net.minecraft.block.BlockState
import net.minecraft.util.JSONUtils
import net.minecraftforge.registries.ForgeRegistryEntry

class HeatSource : ForgeRegistryEntry<HeatSource>() {
    var heatLevel: Int = 0
        private set

    lateinit var heatSource: WorldIngredient
        private set

    fun matches(state: BlockState): Boolean {
        return heatSource.test(state)
    }

    companion object {

        fun deserialize(json: JsonObject): HeatSource {
            if (!json.has("source")) {
                throw JsonSyntaxException("Heat source is missing block!")
            }

            val out = HeatSource()

            out.heatLevel = JSONUtils.getInt(json, RegistriesLib.HEAT)

            out.heatSource = WorldIngredient.deserialize(json.getAsJsonObject("source"))

            return out
        }
    }
}
