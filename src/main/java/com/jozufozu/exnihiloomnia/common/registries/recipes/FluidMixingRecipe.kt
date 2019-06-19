package com.jozufozu.exnihiloomnia.common.registries.recipes

//import com.google.gson.JsonObject
//import com.google.gson.JsonParseException
//import com.google.gson.JsonSyntaxException
//import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
//import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
//import net.minecraft.block.Block
//import net.minecraft.block.Blocks
//import net.minecraft.item.ItemStack
//import net.minecraft.util.JSONUtils
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.SoundEvent
//import net.minecraftforge.fluids.FluidStack
//import net.minecraftforge.registries.ForgeRegistries
//import net.minecraftforge.registries.ForgeRegistryEntry
//
//class FluidMixingRecipe {
//    /**
//     * What's in the barrel
//     */
//    lateinit var lower: FluidStack
//        private set
//
//    /**
//     * What goes on the barrel
//     */
//    lateinit var upper: FluidStack
//        private set
//
//    var output: ItemStack = ItemStack.EMPTY
//        get() = output.copy()
//        private set
//
//    lateinit var craftSound: SoundEvent
//        private set
//
//    fun matches(lower: FluidStack, upper: FluidStack): Boolean {
//        return this.upper.isFluidEqual(upper) && this.lower.isFluidEqual(lower)
//    }
//
//    companion object {
//
//        @Throws(JsonParseException::class)
//        fun deserialize(json: JsonObject): FluidMixingRecipe {
//            if (!json.has(RegistriesLib.OUTPUT_BLOCK)) {
//                throw JsonSyntaxException("Fluid mixing recipe is missing output!")
//            }
//
//            val out = FluidMixingRecipe()
//
//            out.lower = FluidStack(JsonHelper.deserializeFluid(json, RegistriesLib.IN_BARREL), 1000)
//            out.upper = FluidStack(JsonHelper.deserializeFluid(json, RegistriesLib.ON_BARREL), 1000)
//
//            out.output = JsonHelper.deserializeItem(json.getAsJsonObject(RegistriesLib.OUTPUT_BLOCK), true)
//
//            out.craftSound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(JSONUtils.getString(json, "sound", "minecraft:block.lava.extinguish")))!!
//
//            if (Block.getBlockFromItem(out.output.item) === Blocks.AIR) {
//                throw JsonSyntaxException("Recipe does not output a block, ignoring")
//            }
//
//            return out
//        }
//    }
//}
