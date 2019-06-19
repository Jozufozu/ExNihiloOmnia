package com.jozufozu.exnihiloomnia.common.registries.recipes

//import com.google.gson.JsonObject
//import com.google.gson.JsonParseException
//import com.google.gson.JsonSyntaxException
//import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
//import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
//import net.minecraft.item.ItemStack
//import net.minecraft.item.crafting.Ingredient
//import net.minecraft.util.JSONUtils
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.SoundEvent
//import net.minecraftforge.fluids.FluidStack
//import net.minecraftforge.registries.ForgeRegistries
//import net.minecraftforge.registries.ForgeRegistryEntry
//
//class FluidCraftingRecipe {
//    /**
//     * The fluid that is in the barrel
//     */
//    lateinit var fluid: FluidStack
//        private set
//
//    /**
//     * The item required to trigger the crafting
//     */
//    lateinit var catalyst: Ingredient
//        private set
//
//    /**
//     * The item that you get from crafting
//     */
//    lateinit var result: ItemStack
//        private set
//
//    lateinit var craftSound: SoundEvent
//        private set
//
//    fun matches(catalyst: ItemStack, fluid: FluidStack): Boolean {
//        return this.catalyst.test(catalyst) && this.fluid.isFluidEqual(fluid)
//    }
//
//    companion object {
//
//        @Throws(JsonParseException::class)
//        fun deserialize(json: JsonObject): FluidCraftingRecipe {
//            if (!json.has(RegistriesLib.INPUT)) {
//                throw JsonSyntaxException("Fluid crafting recipe is missing input!")
//            }
//
//            if (!json.has(RegistriesLib.OUTPUT)) {
//                throw JsonSyntaxException("Fluid crafting recipe is missing output!")
//            }
//
//            val out = FluidCraftingRecipe()
//
//            out.catalyst = JsonHelper.deserializeIngredient(json.getAsJsonObject(RegistriesLib.INPUT))
//
//            out.fluid = FluidStack(JsonHelper.deserializeFluid(json, RegistriesLib.INPUT_FLUID), 1000)
//
//            out.craftSound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(JSONUtils.getString(json, "sound", "minecraft:entity.bobber.splash")))!!
//
//            out.result = JsonHelper.deserializeItem(json.getAsJsonObject(RegistriesLib.OUTPUT), true)
//
//            return out
//        }
//    }
//}
