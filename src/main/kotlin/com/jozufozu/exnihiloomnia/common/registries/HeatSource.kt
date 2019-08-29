package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import net.minecraft.block.state.IBlockState
import net.minecraft.util.JsonUtils
import net.minecraftforge.registries.IForgeRegistryEntry

class HeatSource : IForgeRegistryEntry.Impl<HeatSource>() {
    var heatLevel: Int = 0
        private set

    private lateinit var heatSource: WorldIngredient

    fun matches(state: IBlockState): Boolean {
        return heatSource.test(state)
    }

    companion object Serde {

        fun deserialize(jsonObject: JsonObject): HeatSource {
            if (!jsonObject.has("source")) {
                throw JsonSyntaxException("Heat source is missing block!")
            }

            val out = HeatSource()

            out.heatLevel = JsonUtils.getInt(jsonObject, LibRegistries.HEAT)

            out.heatSource = WorldIngredient.deserialize(jsonObject.getAsJsonObject("source"))

            return out
        }
    }
}
