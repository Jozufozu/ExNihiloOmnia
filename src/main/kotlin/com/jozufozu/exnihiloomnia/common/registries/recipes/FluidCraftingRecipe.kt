package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

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
        val craftSound: SoundEvent = SoundEvents.ENTITY_FISHING_BOBBER_SPLASH
) : ForgeRegistryEntry<FluidCraftingRecipe>() {

    /**
     * The item that you get from crafting
     */
    val output: ItemStack = output
        get() = field.copy()

    fun matches(catalyst: ItemStack, fluid: FluidStack): Boolean {
        return this.catalyst.test(catalyst) && this.fluid.isFluidEqual(fluid)
    }

    companion object Serde {

        private const val DEFAULT_SOUND: String = "minecraft:entity.bobber.splash"

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): FluidCraftingRecipe? {
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.INPUT}\"")
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.OUTPUT !in recipe) throw JsonSyntaxException("fluid crafting recipe is missing \"${LibRegistries.OUTPUT}\"")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS)) return null

            val catalyst = CraftingHelper.getIngredient(JSONUtils.getJsonObject(recipe, LibRegistries.INPUT))

            val fluid = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            val soundName = JSONUtils.getString(recipe, "sound", DEFAULT_SOUND)
            val craftSound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(soundName))

            val result = JsonHelper.deserializeItem(JSONUtils.getJsonObject(recipe, LibRegistries.OUTPUT), true)

            if (craftSound == null) {
                RegistryLoader.warn("$soundName is not a valid sound, defaulting to $DEFAULT_SOUND")
                return FluidCraftingRecipe(fluid, catalyst, result)
            }

            return FluidCraftingRecipe(fluid, catalyst, result, craftSound)
        }
    }
}
