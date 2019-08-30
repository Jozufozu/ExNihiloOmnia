package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FluidCraftingRecipe(
        /**
         * The fluid that is in the barrel
         */
        val fluid: FluidStack,

        /**
         * The item required to trigger the crafting
         */
        val catalyst: Ingredient,
        output: ItemStack,
        val craftSound: SoundEvent = SoundEvents.ENTITY_BOBBER_SPLASH
) : IForgeRegistryEntry.Impl<FluidCraftingRecipe>() {

    /**
     * The item that you get from crafting
     */
    val output: ItemStack = output
        get() = field.copy()

    fun matches(catalyst: ItemStack, fluid: FluidStack): Boolean {
        return this.catalyst.apply(catalyst) && this.fluid.isFluidEqual(fluid)
    }

    companion object Serde {

        private const val DEFAULT_SOUND: String = "minecraft:entity.bobber.splash"

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): FluidCraftingRecipe {
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.INPUT}\"")
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.OUTPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.OUTPUT}\"")

            val catalyst = CraftingHelper.getIngredient(JsonUtils.getJsonObject(recipe, LibRegistries.INPUT), RegistryLoader.CONTEXT)

            val fluid = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            val soundName = JsonUtils.getString(recipe, "sound", DEFAULT_SOUND)
            val craftSound = SoundEvent.REGISTRY.getObject(ResourceLocation(soundName))

            val result = JsonHelper.deserializeItem(JsonUtils.getJsonObject(recipe, LibRegistries.OUTPUT), true)

            if (craftSound == null) {
                RegistryLoader.warn("$soundName is not a valid sound, defaulting to $DEFAULT_SOUND")
                return FluidCraftingRecipe(fluid, catalyst, result)
            }

            return FluidCraftingRecipe(fluid, catalyst, result, craftSound)
        }
    }
}
