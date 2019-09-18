package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class LeakingRecipe(
        val block: WorldIngredient,
        val fluid: FluidStack,
        val chance: Float,
        val result: IBlockState
        ) : IForgeRegistryEntry.Impl<LeakingRecipe>() {

    fun matches(fluidStack: FluidStack): Boolean = fluid.isFluidEqual(fluidStack)
    fun matches(state: IBlockState): Boolean = block.test(state)

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(recipe: JsonObject): LeakingRecipe? {
            if (LibRegistries.WORLD_INPUT !in recipe) throw JsonSyntaxException("leaking recipe is missing \"${LibRegistries.WORLD_INPUT}\"")
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("leaking recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.RESULT !in recipe) throw JsonSyntaxException("leaking recipe is missing \"${LibRegistries.RESULT}\"")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            val chance = JsonUtils.getFloat(recipe, LibRegistries.CHANCE, 0.00006f)

            RegistryLoader.pushCtx(LibRegistries.WORLD_INPUT)
            val block = WorldIngredient.deserialize(recipe[LibRegistries.WORLD_INPUT])

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_INPUT)
            val fluid = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            RegistryLoader.pushPopCtx(LibRegistries.RESULT)
            val state = when (val element = recipe[LibRegistries.RESULT]) {
                is JsonPrimitive -> {
                    if (!element.isString) throw JsonSyntaxException("")

                    val block = Block.getBlockFromName(element.asString) ?: throw JsonParseException("'$element' is not a valid block")

                    block.defaultState
                }
                is JsonObject -> {
                    val blockName = JsonUtils.getString(element, LibRegistries.BLOCK)
                    val block = Block.getBlockFromName(blockName) ?: throw JsonParseException("'$blockName' is not a valid block")

                    val meta = JsonUtils.getInt(element, LibRegistries.DATA, 0)

                    block.getStateFromMeta(meta)
                }
                else -> throw JsonSyntaxException("must be either a string or a JsonObject")
            }

            return LeakingRecipe(block, fluid, chance, state)
        }
    }
}
