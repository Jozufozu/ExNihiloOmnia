package com.jozufozu.exnihiloomnia.common.ores

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.util.NonNullList
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import java.util.*

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object OreManager {
    val items = hashMapOf<Ore, Map<ItemType, ItemOre>>()
    val blocks = hashMapOf<Ore, Map<BlockType, BlockOre>>()

    fun initOreDict() {
        for ((ore, map) in items.entries) {
            for (itemOre in map.values) {
                ore.itemOreDict[itemOre.type]?.forEach { OreDictionary.registerOre(it, itemOre) }
            }
        }

        for ((ore, map) in blocks.entries) {
            for (blockOre in map.values) {
                ore.blockOreDict[blockOre.type]?.forEach { OreDictionary.registerOre(it, blockOre) }
            }
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        for (ore in RegistryManager.ORES) {
            val map = EnumMap<ItemType, ItemOre>(ItemType::class.java)
            ItemType.values().forEach {
                if (ore.hasItem(it)) {
                    map[it] = ItemOre(ore, it).also(event.registry::register)
                }
            }
            items[ore] = map
        }

        blocks.asSequence().flatMap { it.value.values.asSequence() }.map { it.itemBlock }.forEach(event.registry::register)
    }

    @SubscribeEvent
    @JvmStatic fun registerBlocks(event: RegistryEvent.Register<Block>) {
        for (ore in RegistryManager.ORES) {
            val map = EnumMap<BlockType, BlockOre>(BlockType::class.java)
            BlockType.values().forEach {
                if (ore.hasBlock(it)) {
                    map[it] = BlockOre(ore, it).also(event.registry::register)
                }
            }
            blocks[ore] = map
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
        for ((ore, map) in items.entries) {
            for (itemOre in map.values) {
                val blockType = itemOre.type.blockEquivalent
                if (blockType != null) {
                    blocks[ore]?.get(blockType)?.let { blockOre ->
                        event.registry.register(ShapedRecipes(blockType.toString(), 2, 2, NonNullList.withSize(4, Ingredient.fromItem(itemOre)), ItemStack(blockOre)).also { it.registryName = blockOre.registryName })
                    }
                }
            }
        }

        for ((ore, map) in blocks.entries) {
            val ingot = if (ore.hasGeneratedIngot) {
                val item = items[ore]?.get(ItemType.INGOT)
                if (item == null) ItemStack.EMPTY
                else ItemStack(item)
            } else ore.ingot

            if (ingot.isEmpty) continue

            for (blockOre in map.values) {
                GameRegistry.addSmelting(blockOre, ingot, ore.smeltingExp)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerModels(event: ModelRegistryEvent) {
        items.asSequence().flatMap { it.value.values.asSequence() }.forEach {
            val name = it.ore.getName(it.type)
            val model = ModelResourceLocation("${name.resourceDomain}:ores/item/${name.resourcePath}")
            ModelLoader.setCustomModelResourceLocation(it, 0, model)
        }

        blocks.asSequence().flatMap { it.value.values.asSequence() }.map { it to it.itemBlock }.forEach {
            val name = it.first.ore.getName(it.first.type)
            val model = ModelResourceLocation("${name.resourceDomain}:ores/block/${name.resourcePath}")
            ModelLoader.setCustomModelResourceLocation(it.second, 0, model)
        }

        blocks.asSequence().flatMap { it.value.values.asSequence() }.forEach { ModelLoader.setCustomStateMapper(it) { _ ->
            val name = it.ore.getName(it.type)
            val model = ModelResourceLocation("${name.resourceDomain}:ores/block/${name.resourcePath}")
            mapOf(it.defaultState to model)
        } }
    }

    val ORES_TAB: CreativeTabs = object : CreativeTabs("exnihiloomnia.ores") {
        override fun getTabIconItem(): ItemStack {
            return ItemStack(items.entries.first().value.entries.first().value)
        }

        override fun displayAllRelevantItems(list: NonNullList<ItemStack>) {
            RegistryManager.ORES.asSequence().flatMap {
                blocks[it]?.values?.asSequence()?.map { ItemStack(it) }.orEmpty() +
                        items[it]?.values?.asSequence()?.map { ItemStack(it) }.orEmpty()
            }.toCollection(list)
        }
    }
}