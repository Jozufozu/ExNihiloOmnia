package com.jozufozu.exnihiloomnia.common.registries

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ExNihiloLootParameterSets
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe
import com.jozufozu.exnihiloomnia.common.util.Color
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.recipe.Ingredient
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloadListener
import net.minecraft.resources.IResourceManager
import net.minecraft.resources.IResourceManagerReloadListener
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.NonNullList
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.loot.context.LootContext
import net.minecraft.world.loot.context.LootContextParameters
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.loot.LootContext
import net.minecraft.world.storage.loot.LootParameter
import net.minecraft.world.storage.loot.LootParameters
import net.minecraftforge.resource.IResourceType
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.*
import java.util.function.Predicate

object RegistryManager : ISelectiveResourceReloadListener {
    val GSON: Gson = with(GsonBuilder()) {
        registerTypeAdapter(Color::class.java, Color.serde)
        registerTypeAdapter<Ingredient> {
            deserialize {
                Ingredient.fromJson(it.json)
            }
        }
        registerTypeAdapter<ItemStack> {
            deserialize {
                CraftingHelper.getItemStack(it.json.asJsonObject, true)
            }
        }
        registerTypeAdapter<WorldIngredient> {
            deserialize {

            }
        }
        registerTypeAdapter(ResourceLocation::class.java, ResourceLocation.Serializer())
        create()
    }

    val ORES: MutableSet<Ore> = Collections.emptySortedSet()

    val COMPOST: MutableSet<CompostRecipe> = Collections.emptySortedSet()
    //val FLUID_CRAFTING: Registry<FluidCraftingRecipe> = Registry()
    //val FLUID_MIXING: Registry<FluidMixingRecipe> = Registry()
    //val FERMENTING: Registry<FermentingRecipe> = Registry()

    val SIFTING: MutableSet<SieveRecipe> = Collections.emptySortedSet()
    val HAMMERING: MutableSet<HammerRecipe> = Collections.emptySortedSet()

    //val MELTING: Registry<MeltingRecipe> = Registry()
    val HEAT: MutableSet<HeatSource> = Collections.emptySortedSet()

    override fun onResourceManagerReload(resourceManager: IResourceManager, resourcePredicate: Predicate<IResourceType>) {
        loadResources(resourceManager, "exnihiloomnia/recipes/composting", "compost recipe", COMPOST, CompostRecipe::class.java)
        loadResources(resourceManager, "exnihiloomnia/recipes/sifting", "sifting recipe", SIFTING, SieveRecipe::class.java)
        loadResources(resourceManager, "exnihiloomnia/recipes/hammering", "hammering recipe", HAMMERING, HammerRecipe::class.java)
        loadResources(resourceManager, "exnihiloomnia/heat", "heat source", HEAT, HeatSource::class.java)
        loadResources(resourceManager, "exnihiloomnia/ores", "ore", ORES, Ore::class.java)
    }

    fun <T> loadResources(resourceManager: IResourceManager, folder: String, name: String, registry: MutableCollection<T>, type: Class<T>) {
        registry.clear()
        val extension = ".json"

        for (file in resourceManager.getAllResourceLocations(folder) { it.endsWith(extension) }) {
            val id = ResourceLocation(file.namespace, file.path.substring(folder.length + 1, file.path.length - extension.length))

            try {
                val resource = resourceManager.getResource(file)

                val recipe = try {
                    GSON.fromJson(InputStreamReader(resource.inputStream), type)
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
            withRandom(world.random)
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
            withRandom(world.random)
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
