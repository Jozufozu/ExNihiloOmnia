package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class MeltingRecipe : IForgeRegistryEntry.Impl<MeltingRecipe>() {
    /**
     * The minimum heat level required for this to melt.
     * Every heat level over the Crucible is will add 1x to the speed multiplier
     */
    var requiredHeat: Int = 0
        private set

    lateinit var input: Ingredient
        private set

    var inputVolume: Int = 0
        private set

    private var _output: FluidStack = FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME)

    val output get() = _output.copy()

    fun matches(input: ItemStack): Boolean {
        return this.input.apply(input)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): MeltingRecipe {
            val out = MeltingRecipe()

            if (!jsonObject.has(LibRegistries.INPUT_GENERIC)) {
                throw JsonSyntaxException("Melting recipe is missing input!")
            }

            val input = jsonObject.getAsJsonObject(LibRegistries.INPUT_GENERIC)

            out.input = JsonHelper.deserializeIngredient(input)
            out.inputVolume = JsonUtils.getInt(input, LibRegistries.VOLUME, 1000)

            out.requiredHeat = JsonUtils.getInt(jsonObject, LibRegistries.HEAT, 1)
            out._output = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.OUTPUT_FLUID), JsonUtils.getInt(jsonObject, LibRegistries.VOLUME, 1000))

            if (out.inputVolume <= 0 || out.inputVolume > ModConfig.blocks.crucible.solidCapacity) {
                throw JsonSyntaxException("Melting recipe has invalid inputVolume, expected range: 1 - " + ModConfig.blocks.crucible.solidCapacity)
            }
            if (out._output.amount <= 0 || out._output.amount > ModConfig.blocks.crucible.fluidCapacity) {
                throw JsonSyntaxException("Melting recipe has invalid outputVolume, expected range: 1 - " + ModConfig.blocks.crucible.fluidCapacity)
            }

            return out
        }
    }
}
