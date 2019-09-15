package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.BarrelStateFermenting
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.state.IBlockState
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.IForgeRegistryEntry

class FermentingRecipe(
        val block: WorldIngredient,
        val toFerment: FluidStack,
        val time: Int,
        val chance: Float,
        output: FluidStack
) : IForgeRegistryEntry.Impl<FermentingRecipe>() {

    val output: FluidStack = output
        get() = field.copy()

    val barrelState by lazy {
        val name = ResourceLocation(registryName!!.resourceDomain, "fermenting_${registryName!!.resourcePath}")
        BarrelStateFermenting(this, name)
    }

    fun matches(fluidStack: FluidStack): Boolean = toFerment.isFluidEqual(fluidStack)
    fun matches(state: IBlockState): Boolean = block.test(state)

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(recipe: JsonObject): FermentingRecipe? {
            if (LibRegistries.WORLD_INPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.WORLD_INPUT}\"")
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.FLUID_OUTPUT !in recipe) throw JsonSyntaxException("fermenting recipe is missing \"${LibRegistries.FLUID_OUTPUT}\"")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            val time = JsonUtils.getInt(recipe, LibRegistries.TIME, 400)
            val chance = JsonUtils.getFloat(recipe, LibRegistries.CHANCE, 0.0005f)

            RegistryLoader.pushCtx(LibRegistries.WORLD_INPUT)
            val block = WorldIngredient.deserialize(recipe[LibRegistries.WORLD_INPUT])

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_INPUT)
            val toFerment = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_OUTPUT)
            val output = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_OUTPUT])

            return FermentingRecipe(block, toFerment, time, chance, output)
        }
    }
}
