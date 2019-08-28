package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FermentingRecipe : IForgeRegistryEntry.Impl<FermentingRecipe>() {
    private var block: WorldIngredient? = null
    private var toFerment: FluidStack? = null
    private var output: FluidStack? = null

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(jsonObject: JsonObject): FermentingRecipe {
            if (!jsonObject.has(LibRegistries.INPUT_BLOCK)) {
                throw JsonSyntaxException("Fermenting recipe is missing block input!")
            }

            val out = FermentingRecipe()

            out.block = WorldIngredient.deserialize(jsonObject.getAsJsonObject(LibRegistries.INPUT_BLOCK))

            out.toFerment = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.INPUT_FLUID), 1000)
            out.output = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.OUTPUT_FLUID), 1000)

            return out
        }
    }
}
