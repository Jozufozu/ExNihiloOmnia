package com.jozufozu.exnihiloomnia.common.registries.recipes

//import com.google.gson.JsonObject
//import com.google.gson.JsonParseException
//import com.google.gson.JsonSyntaxException
//import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
//import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
//import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
//import net.minecraftforge.fluids.FluidStack
//import net.minecraftforge.registries.ForgeRegistryEntry
//
//class FermentingRecipe {
//    lateinit var block: WorldIngredient
//        private set
//    lateinit var toFerment: FluidStack
//        private set
//    lateinit var output: FluidStack
//        private set
//
//    companion object {
//
//        @Throws(JsonParseException::class)
//        fun deserialize(json: JsonObject): FermentingRecipe {
//            if (!json.has(RegistriesLib.INPUT_BLOCK)) {
//                throw JsonSyntaxException("Fermenting recipe is missing block input!")
//            }
//
//            val out = FermentingRecipe()
//
//            out.block = WorldIngredient.deserialize(json.getAsJsonObject(RegistriesLib.INPUT_BLOCK))
//
//            out.toFerment = FluidStack(JsonHelper.deserializeFluid(json, RegistriesLib.INPUT_FLUID), 1000)
//            out.output = FluidStack(JsonHelper.deserializeFluid(json, RegistriesLib.OUTPUT_FLUID), 1000)
//
//            return out
//        }
//    }
//}
