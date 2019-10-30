package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

class FluidMixingRecipe(
        /**
         * What's in the barrel
         */
        val lower: FluidStack,

        /**
         * What goes on the barrel
         */
        val upper: FluidStack,

        output: ItemStack,

        val craftSound: SoundEvent = SoundEvents.BLOCK_LAVA_EXTINGUISH
): ForgeRegistryEntry<FluidMixingRecipe>() {

    val output: ItemStack = output
        get() = field.copy()

    fun matches(lower: FluidStack, upper: FluidStack): Boolean {
        return this.upper.isFluidEqual(upper) && this.lower.isFluidEqual(lower)
    }

    companion object Serde {

        private const val DEFAULT_SOUND: String = "minecraft:block.lava.extinguish"

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): FluidMixingRecipe? {
            if (LibRegistries.OUTPUT !in recipe) throw JsonSyntaxException("fluid mixing recipe is missing \"${LibRegistries.OUTPUT}\"")
            if (LibRegistries.IN_BARREL !in recipe) throw JsonSyntaxException("fluid mixing recipe is missing \"${LibRegistries.IN_BARREL}\"")
            if (LibRegistries.ON_BARREL !in recipe) throw JsonSyntaxException("fluid mixing recipe is missing \"${LibRegistries.ON_BARREL}\"")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS)) return null

            val lower = JsonHelper.deserializeFluid(recipe[LibRegistries.IN_BARREL])
            val upper = JsonHelper.deserializeFluid(recipe[LibRegistries.ON_BARREL])

            val output = JsonHelper.deserializeItem(JSONUtils.getJsonObject(recipe, LibRegistries.OUTPUT), true)

            val soundName = JSONUtils.getString(recipe, "sound", DEFAULT_SOUND)
            val craftSound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(soundName))

            if (craftSound == null) {
                RegistryLoader.warn("'$soundName' is not a valid sound, defaulting to '$DEFAULT_SOUND'")
                return FluidMixingRecipe(lower, upper, output)
            }

            return FluidMixingRecipe(lower, upper, output, craftSound)
        }
    }
}
