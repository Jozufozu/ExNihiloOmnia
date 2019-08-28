package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FluidMixingRecipe : IForgeRegistryEntry.Impl<FluidMixingRecipe>() {
    /**
     * What's in the barrel
     */
    var lower: FluidStack? = null
        private set

    /**
     * What goes on the barrel
     */
    var upper: FluidStack? = null
        private set

    private var _output: ItemStack = ItemStack.EMPTY

    val output: ItemStack get() = _output.copy()

    var craftSound: SoundEvent? = null
        private set

    fun matches(lower: FluidStack, upper: FluidStack): Boolean {
        return this.upper!!.isFluidEqual(upper) && this.lower!!.isFluidEqual(lower)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): FluidMixingRecipe {
            if (!jsonObject.has(LibRegistries.OUTPUT_BLOCK)) {
                throw JsonSyntaxException("Fluid mixing recipe is missing output!")
            }

            val out = FluidMixingRecipe()

            out.lower = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.IN_BARREL), 1000)
            out.upper = FluidStack(JsonHelper.deserializeFluid(jsonObject, LibRegistries.ON_BARREL), 1000)

            out._output = JsonHelper.deserializeItem(jsonObject.getAsJsonObject(LibRegistries.OUTPUT_BLOCK), true)

            out.craftSound = SoundEvent.REGISTRY.getObject(ResourceLocation(JsonUtils.getString(jsonObject, "sound", "minecraft:block.lava.extinguish")))

            if (Block.getBlockFromItem(out._output.item) === Blocks.AIR) {
                throw JsonSyntaxException("Recipe does not output a block, ignoring")
            }

            return out
        }
    }
}
