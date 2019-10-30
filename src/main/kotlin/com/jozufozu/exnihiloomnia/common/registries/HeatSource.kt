package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.BlockState
import net.minecraft.util.JSONUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class HeatSource(
        val heatLevel: Int,
        val heatSource: WorldIngredient
) : ForgeRegistryEntry<HeatSource>() {

    fun matches(state: BlockState) = heatSource.test(state)

    companion object Serde {

        fun deserialize(heatSource: JsonObject): HeatSource? {
            if (LibRegistries.SOURCE !in heatSource) throw JsonSyntaxException("heat source is missing \"${LibRegistries.SOURCE}\"")

            if (!CraftingHelper.processConditions(heatSource, LibRegistries.CONDITIONS)) return null

            val heatLevel = JSONUtils.getInt(heatSource, LibRegistries.HEAT)

            RegistryLoader.pushCtx(LibRegistries.SOURCE)
            val source = WorldIngredient.deserialize(heatSource[LibRegistries.SOURCE])
            RegistryLoader.popCtx()

            return HeatSource(heatLevel, source)
        }
    }
}
