package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.state.IBlockState
import net.minecraft.util.JsonUtils
import net.minecraftforge.registries.IForgeRegistryEntry

class HeatSource(
        val heatLevel: Int,
        val heatSource: WorldIngredient
) : IForgeRegistryEntry.Impl<HeatSource>() {

    fun matches(state: IBlockState) = heatSource.test(state)

    companion object Serde {

        fun deserialize(heatSource: JsonObject): HeatSource {
            if (LibRegistries.SOURCE !in heatSource) throw JsonSyntaxException("Heat source is missing block!")

            val heatLevel = JsonUtils.getInt(heatSource, LibRegistries.HEAT)

            RegistryLoader.pushCtx(LibRegistries.SOURCE)
            val source = WorldIngredient.deserialize(JsonUtils.getJsonObject(heatSource, LibRegistries.SOURCE))
            RegistryLoader.popCtx()

            return HeatSource(heatLevel, source)
        }
    }
}
