package com.jozufozu.exnihiloomnia.common.registries.recipes

//import com.google.gson.JsonObject
//import com.google.gson.JsonParseException
//import com.google.gson.JsonSyntaxException
//import com.jozufozu.exnihiloomnia.common.ModConfig
//import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
//import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
//import net.minecraft.item.ItemStack
//import net.minecraft.item.crafting.Ingredient
//import net.minecraft.util.JsonUtils
//import net.minecraftforge.fluids.FluidStack
//import net.minecraftforge.registries.ForgeRegistryEntry
//
//class MeltingRecipe : ForgeRegistryEntry<MeltingRecipe>() {
//    /**
//     * The minimum heat level required for this to melt.
//     * Every heat level over the Crucible is will add 1x to the speed multiplier
//     */
//    var requiredHeat: Int = 0
//        private set
//
//    val input: Ingredient? = null
//
//    var inputVolume: Int = 0
//        private set
//
//    var output: FluidStack? = FluidStack
//
//    fun getOutput(): FluidStack {
//        return output!!.copy()
//    }
//
//    fun matches(input: ItemStack): Boolean {
//        return this.input!!.apply(input)
//    }
//
//    companion object {
//
//        @Throws(JsonParseException::class)
//        fun deserialize(`object`: JsonObject): MeltingRecipe {
//            val out = MeltingRecipe()
//
//            if (!`object`.has(RegistriesLib.INPUT)) {
//                throw JsonSyntaxException("Melting recipe is missing input!")
//            }
//
//            val input = `object`.getAsJsonObject(RegistriesLib.INPUT)
//
//            out.input = JsonHelper.deserializeIngredient(input)
//            out.inputVolume = JsonUtils.getInt(input, RegistriesLib.VOLUME, 1000)
//
//            out.requiredHeat = JsonUtils.getInt(`object`, RegistriesLib.HEAT, 1)
//            out.output = FluidStack(JsonHelper.deserializeFluid(`object`, RegistriesLib.OUTPUT_FLUID), JsonUtils.getInt(`object`, RegistriesLib.VOLUME, 1000))
//
//            if (out.inputVolume <= 0 || out.inputVolume > ModConfig.blocks.crucible.solidCapacity) {
//                throw JsonSyntaxException("Melting recipe has invalid inputVolume, expected range: 1 - " + ModConfig.blocks.crucible.solidCapacity)
//            }
//            if (out.output!!.amount <= 0 || out.output!!.amount > ModConfig.blocks.crucible.fluidCapacity) {
//                throw JsonSyntaxException("Melting recipe has invalid outputVolume, expected range: 1 - " + ModConfig.blocks.crucible.fluidCapacity)
//            }
//
//            return out
//        }
//    }
//}
