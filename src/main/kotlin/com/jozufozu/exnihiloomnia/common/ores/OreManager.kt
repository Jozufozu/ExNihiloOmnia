package com.jozufozu.exnihiloomnia.common.ores

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.util.NonNullList
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object OreManager {
    val items = hashMapOf<Ore, Map<ItemType, ItemOre>>()
    val blocks = hashMapOf<Ore, Map<BlockType, BlockOre>>()

    @SubscribeEvent
    @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        for (ore in RegistryManager.ORES) {
            ItemType.values()
                    .filter(ore::hasItem)
                    .map { it to ItemOre(ore, it).also(event.registry::register) }
                    .toMap()
                    .let { items.put(ore, it) }
        }

        blocks.flatMap { it.value.values }.map { it.itemBlock }.forEach(event.registry::register)
    }

    @SubscribeEvent
    @JvmStatic fun registerBlocks(event: RegistryEvent.Register<Block>) {
        for (ore in RegistryManager.ORES) {
            BlockType.values()
                    .filter(ore::hasBlock)
                    .map { it to BlockOre(ore, it).also(event.registry::register) }
                    .toMap()
                    .let { blocks.put(ore, it) }
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
        items.flatMap { it.value.values }
                .mapNotNull { blocks[it.ore]?.get(it.type.blockEquivalent)?.to(it) }
                .map { ShapedRecipes(it.first.toString(), 2, 2, NonNullList.withSize(4, Ingredient.fromItem(it.second)), ItemStack(it.first.itemBlock)) }
                .forEach(event.registry::register)
    }
}