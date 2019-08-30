package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.util.JsonUtils
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FermentingRecipe(
        val block: WorldIngredient,
        val toFerment: FluidStack,
        output: FluidStack
) : IForgeRegistryEntry.Impl<FermentingRecipe>() {

    val output: FluidStack = output
        get() = field.copy()

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(recipe: JsonObject): FermentingRecipe {
            if (LibRegistries.WORLD_INPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.WORLD_INPUT}\"")
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.FLUID_OUTPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.FLUID_OUTPUT}\"")

            RegistryLoader.pushCtx(LibRegistries.WORLD_INPUT)
            val block = WorldIngredient.deserialize(JsonUtils.getJsonObject(recipe, LibRegistries.WORLD_INPUT))

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_INPUT)
            val toFerment = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_OUTPUT)
            val output = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_OUTPUT])

            return FermentingRecipe(block, toFerment, output)
        }
    }
}
