package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ExNihiloLootParameterSets
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.resources.IResourceManager
import net.minecraft.resources.IResourceManagerReloadListener
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.ServerWorld
import net.minecraft.world.storage.loot.LootContext
import net.minecraft.world.storage.loot.LootParameters
import java.io.InputStreamReader
import java.util.*

typealias Registry<T> =  HashMap<ResourceLocation, T>

object RegistryManager : IResourceManagerReloadListener {
    val GSON = GsonBuilder().create()

    val ORES: MutableSet<Ore> = Collections.emptySortedSet()

    val COMPOST: MutableSet<CompostRecipe> = Collections.emptySortedSet()
    //val FLUID_CRAFTING: Registry<FluidCraftingRecipe> = Registry()
    //val FLUID_MIXING: Registry<FluidMixingRecipe> = Registry()
    //val FERMENTING: Registry<FermentingRecipe> = Registry()

    val SIFTING: MutableSet<SieveRecipe> = Collections.emptySortedSet()
    val HAMMERING: MutableSet<HammerRecipe> = Collections.emptySortedSet()

    //val MELTING: Registry<MeltingRecipe> = Registry()
    val HEAT: MutableSet<HeatSource> = Collections.emptySortedSet()

    override fun onResourceManagerReload(resourceManager: IResourceManager) {
        loadResources(resourceManager, "exnihiloomnia/recipes/composting", "compost recipe", COMPOST, CompostRecipe.Companion::deserialize)
        loadResources(resourceManager, "exnihiloomnia/recipes/sifting", "sifting recipe", SIFTING, SieveRecipe.Companion::deserialize)
        loadResources(resourceManager, "exnihiloomnia/recipes/hammering", "hammering recipe", HAMMERING, HammerRecipe.Companion::deserialize)
        loadResources(resourceManager, "exnihiloomnia/heat", "heat source", HEAT, HeatSource.Companion::deserialize)
        loadResources(resourceManager, "exnihiloomnia/ores", "ore", ORES, Ore.Companion::deserialize)
    }

    fun <T> loadResources(resourceManager: IResourceManager, folder: String, name: String, registry: MutableCollection<T>, deserializer: (JsonObject) -> T) {
        registry.clear()
        val extension = ".json"

        for (file in resourceManager.getAllResourceLocations(folder) { it.endsWith(extension) }) {
            val id = ResourceLocation(file.namespace, file.path.substring(folder.length + 1, file.path.length - extension.length))

            try {
                val resource = resourceManager.getResource(file)

                val recipe = try {
                    deserializer(GSON.fromJson(InputStreamReader(resource.inputStream), JsonObject::class.java))
                } catch (e: Exception) {
                    ExNihilo.log.error("Error parsing $name $id")
                    continue
                }

                registry.add(recipe)
            } catch (e: Exception) {
                ExNihilo.log.error("Could not load resource $file", e)
            }
        }
    }

    fun getHammerRewards(world: ServerWorld, hammer: ItemStack, player: PlayerEntity, toHammer: BlockState): NonNullList<ItemStack> {
        val drops = NonNullList.create<ItemStack>()

        if (toHammer.block === Blocks.AIR)
            return drops

        val lootTableManager = world.server.lootTableManager
        val context = with(LootContext.Builder(world)) {
            withLuck(player.luck)
            withParameter(LootParameters.TOOL, hammer)
            withRandom(world.rand)
            build(ExNihiloLootParameterSets.HAMMERING)
        }

        for (hammerRecipe in HAMMERING) {
            if (hammerRecipe.matches(toHammer)) {
                drops.addAll(lootTableManager.getLootTableFromLocation(hammerRecipe.rewards).generate(context))
            }
        }

        return drops
    }

    fun getSieveRewards(world: ServerWorld, mesh: ItemStack, player: PlayerEntity, contents: ItemStack): NonNullList<ItemStack> {
        val drops = NonNullList.create<ItemStack>()

        if (contents.isEmpty)
            return drops

        val lootTableManager = world.server.lootTableManager
        val context = with(LootContext.Builder(world)) {
            withLuck(player.luck)
            withParameter(LootParameters.TOOL, mesh) //TODO: Change this to use a custom parameter, maybe
            withRandom(world.rand)
            build(ExNihiloLootParameterSets.HAMMERING)
        }

        for (recipe in SIFTING) {
            if (recipe.matches(contents)) {
                drops.addAll(lootTableManager.getLootTableFromLocation(recipe.loot).generate(context))
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
    fun hammerable(input: BlockState): Boolean {
        for (recipe in HAMMERING) {
            if (recipe.matches(input))
                return true
        }

        return false
    }

    fun getCompost(input: ItemStack): CompostRecipe? {
        for (recipe in COMPOST) {
            if (recipe.matches(input))
                return recipe
        }

        return null
    }

//    fun getMixing(`in`: FluidStack, on: FluidStack): FluidMixingRecipe? {
//        for ((_, recipe) in FLUID_MIXING) {
//            if (recipe.matches(`in`, on))
//                return recipe
//        }
//
//        return null
//    }
//
//    fun getFluidCrafting(catalyst: ItemStack, fluidStack: FluidStack): FluidCraftingRecipe? {
//        for ((_, recipe) in FLUID_CRAFTING) {
//            if (recipe.matches(catalyst, fluidStack))
//                return recipe
//        }
//
//        return null
//    }
//
//    fun getMelting(input: ItemStack): MeltingRecipe? {
//        for ((_, recipe) in MELTING) {
//            if (recipe.matches(input))
//                return recipe
//        }
//
//        return null
//    }

    fun getHeat(source: BlockState): Int {
        for (heatSource in HEAT) {
            if (heatSource.matches(source))
                return heatSource.heatLevel
        }

        return 0
    }
}
