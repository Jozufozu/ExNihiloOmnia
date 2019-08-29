package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FluidCraftingRecipe : IForgeRegistryEntry.Impl<FluidCraftingRecipe>() {
    /**
     * The fluid that is in the barrel
     */
    lateinit var fluid: FluidStack
        private set

    /**
     * The item required to trigger the crafting
     */
    lateinit var catalyst: Ingredient
        private set

    /**
     * The item that you get from crafting
     */
    var result: ItemStack = ItemStack.EMPTY
        private set

    lateinit var craftSound: SoundEvent
        private set

    fun matches(catalyst: ItemStack, fluid: FluidStack): Boolean {
        return this.catalyst.apply(catalyst) && this.fluid.isFluidEqual(fluid)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): FluidCraftingRecipe {
            if (!jsonObject.has(LibRegistries.INPUT_GENERIC)) {
                throw JsonSyntaxException("Fluid crafting recipe is missing input!")
            }

            if (!jsonObject.has(LibRegistries.OUTPUT_GENERIC)) {
                throw JsonSyntaxException("Fluid crafting recipe is missing output!")
            }

            val out = FluidCraftingRecipe()

            out.catalyst = JsonHelper.deserializeIngredient(jsonObject.getAsJsonObject(LibRegistries.INPUT_GENERIC))

            out.fluid = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.INPUT_FLUID), 1000)

            out.craftSound = SoundEvent.REGISTRY.getObject(ResourceLocation(JsonUtils.getString(jsonObject, "sound", "minecraft:entity.bobber.splash")))!!

            out.result = JsonHelper.deserializeItem(jsonObject.getAsJsonObject(LibRegistries.OUTPUT_GENERIC), true)

            return out
        }
    }
}
