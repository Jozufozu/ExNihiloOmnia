package com.jozufozu.exnihiloomnia.common.registries

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import com.jozufozu.exnihiloomnia.common.registries.recipes.*
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object RegistryManager {
    val ORES = ReloadableRegistry(LibRegistries.ORE, Ore::class.java) { RegistryLoader.genericLoad(it, "/registries/ores", Ore.Serde::deserialize) }

    val COMPOST = ReloadableRegistry(LibRegistries.COMPOST, CompostRecipe::class.java) { RegistryLoader.genericLoad(it, "/registries/composting", CompostRecipe.Serde::deserialize) }
    val FLUID_CRAFTING = ReloadableRegistry(LibRegistries.FLUID_CRAFTING, FluidCraftingRecipe::class.java)  { RegistryLoader.genericLoad(it, "/registries/fluidcrafting", FluidCraftingRecipe.Serde::deserialize) }
    val FLUID_MIXING = ReloadableRegistry(LibRegistries.MIXING, FluidMixingRecipe::class.java)  { RegistryLoader.genericLoad(it, "/registries/fluidmixing", FluidMixingRecipe.Serde::deserialize) }
    val FERMENTING = ReloadableRegistry(LibRegistries.FERMENTING, FermentingRecipe::class.java)  {
        RegistryLoader.genericLoad(it, "/registries/fermenting", FermentingRecipe.Serde::deserialize)
        BarrelStates.setShouldReload()
    }
    val SUMMONING = ReloadableRegistry(LibRegistries.SUMMONING, SummoningRecipe::class.java)  {
        RegistryLoader.genericLoad(it, "/registries/summoning", SummoningRecipe.Serde::deserialize)
        BarrelStates.setShouldReload()
    }

    val SIFTING = ReloadableRegistry(LibRegistries.SIEVE, SieveRecipe::class.java)  { RegistryLoader.genericLoad(it, "/registries/sifting", SieveRecipe.Serde::deserialize) }
    val HAMMERING = ReloadableRegistry(LibRegistries.HAMMER, HammerRecipe::class.java)  { RegistryLoader.genericLoad(it, "/registries/hammering", HammerRecipe.Serde::deserialize) }

    val MELTING = ReloadableRegistry(LibRegistries.MELTING, MeltingRecipe::class.java)  { RegistryLoader.genericLoad(it, "/registries/melting", MeltingRecipe.Serde::deserialize) }
    val HEAT = ReloadableRegistry(LibRegistries.HEAT_SOURCE, HeatSource::class.java)  { RegistryLoader.genericLoad(it, "/registries/heat", HeatSource.Serde::deserialize) }

    fun getHammerRewards(world: World, hammer: ItemStack, player: EntityPlayer, toHammer: IBlockState): NonNullList<ItemStack> {
        val drops = NonNullList.create<ItemStack>()

        if (toHammer.block === Blocks.AIR)
            return drops

        for (hammerRecipe in HAMMERING) {
            if (hammerRecipe.matches(toHammer)) {
                drops.addAll(hammerRecipe.rewards.roll(player, hammer, world.rand))
            }
        }

        return drops
    }

    /**
     * Whether or not the given input can generate rewards
     */
    fun siftable(input: ItemStack): Boolean {
        for (recipe in SIFTING) {
            if (recipe.matches(input))
                return true
        }

        return false
    }

    /**
     * Whether or not the given block can be hammered
     */
    fun hammerable(input: IBlockState): Boolean {
        for (recipe in HAMMERING) {
            if (recipe.matches(input))
                return true
        }

        return false
    }

    fun getFermenting(fluid: FluidStack): List<FermentingRecipe> {
        return FERMENTING.filter { it.matches(fluid) }
    }

    fun getCompost(input: ItemStack): CompostRecipe? {
        for (recipe in COMPOST) {
            if (recipe.matches(input))
                return recipe
        }

        return null
    }

    fun getMixing(fluidIn: FluidStack, fluidOn: FluidStack): FluidMixingRecipe? {
        for (recipe in FLUID_MIXING) {
            if (recipe.matches(fluidIn, fluidOn))
                return recipe
        }

        return null
    }

    fun getFluidCrafting(catalyst: ItemStack, fluidStack: FluidStack): FluidCraftingRecipe? {
        for (recipe in FLUID_CRAFTING) {
            if (recipe.matches(catalyst, fluidStack))
                return recipe
        }

        return null
    }

    fun getSummoning(catalyst: ItemStack, fluidStack: FluidStack): SummoningRecipe? {
        for (recipe in SUMMONING) {
            if (recipe.matches(catalyst, fluidStack))
                return recipe
        }

        return null
    }

    fun getMelting(input: ItemStack): MeltingRecipe? {
        for (recipe in MELTING) {
            if (recipe.matches(input))
                return recipe
        }

        return null
    }

    fun getHeat(source: IBlockState): Int {
        for (heatSource in HEAT) {
            if (heatSource.matches(source))
                return heatSource.heatLevel
        }

        return 0
    }
}
