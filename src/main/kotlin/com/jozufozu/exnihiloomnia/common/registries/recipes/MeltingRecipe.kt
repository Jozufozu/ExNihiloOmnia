package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class MeltingRecipe(
        /**
         * The minimum heat level required for this to melt.
         * Every heat level over the Crucible is will add 1x to the speed multiplier
         */
        val requiredHeat: Int,
        val input: Ingredient,
        val inputVolume: Int,
        output: FluidStack
) : IForgeRegistryEntry.Impl<MeltingRecipe>() {

    val output: FluidStack = output
        get() = field.copy()

    fun matches(input: ItemStack): Boolean {
        return this.input.apply(input)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): MeltingRecipe {
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("melting recipe is missing \"${LibRegistries.INPUT}\"")
            if (LibRegistries.FLUID_OUTPUT !in recipe) throw JsonSyntaxException("melting recipe is missing \"${LibRegistries.FLUID_OUTPUT}\"")

            val input = CraftingHelper.getIngredient(recipe[LibRegistries.INPUT], RegistryLoader.CONTEXT)
            val inputVolume = JsonUtils.getInt(recipe, LibRegistries.VOLUME, 1000)

            val requiredHeat = JsonUtils.getInt(recipe, LibRegistries.HEAT, 1)

            val output = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_OUTPUT])

            if (inputVolume <= 0 || inputVolume > ModConfig.blocks.crucible.solidCapacity) {
                throw JsonSyntaxException("melting recipe has invalid ${LibRegistries.VOLUME}, expected range: [1, ${ModConfig.blocks.crucible.solidCapacity}]")
            }
            if (output.amount <= 0 || output.amount > ModConfig.blocks.crucible.fluidCapacity) {
                throw JsonSyntaxException("melting recipe has invalid ${LibRegistries.FLUID_OUTPUT}, expected range: [1, ${ModConfig.blocks.crucible.fluidCapacity}]")
            }

            return MeltingRecipe(requiredHeat, input, inputVolume, output)
        }
    }
}
