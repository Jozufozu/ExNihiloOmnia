package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.*

class Mesh(
        private val item: Ingredient,
        val multipliers: Map<String, Float>
) : IForgeRegistryEntry.Impl<Mesh>() {

    fun matches(itemStack: ItemStack) = item.test(itemStack)

    companion object Serde {

        fun deserialize(mesh: JsonObject): Mesh? {
            if (LibRegistries.INPUT !in mesh) throw JsonSyntaxException("mesh is missing \"${LibRegistries.INPUT}\"")
            if ("multipliers" !in mesh) throw JsonSyntaxException("mesh is missing \"multipliers\"")

            if (!CraftingHelper.processConditions(mesh, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            RegistryLoader.pushCtx(LibRegistries.INPUT)
            val input = CraftingHelper.getIngredient(mesh.get(LibRegistries.INPUT), RegistryLoader.CONTEXT)

            val multipliersJson = JsonUtils.getJsonObject(mesh, "multipliers")

            val multipliers = HashMap<String, Float>()

            RegistryLoader.pushPopCtx("multipliers")
            for ((type, value) in multipliersJson.entrySet()) {
                RegistryLoader.pushCtx(type)
                if (value is JsonPrimitive && value.isNumber) {
                    multipliers[type] = value.asFloat
                } else {
                    RegistryLoader.error("multiplier must be number")
                }
                RegistryLoader.popCtx()
            }

            return Mesh(input, multipliers)
        }
    }
}