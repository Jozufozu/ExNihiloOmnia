package com.jozufozu.exnihiloomnia.common.registries.ores

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.ores.BlockType
import com.jozufozu.exnihiloomnia.common.ores.ItemType
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * The registry name is the base [ResourceLocation] that all registered blocks and items will be given
 * This is usually going to be the json file name and location
 * Ex. mod:ore will create:
 * mod:ore_gravel_ore
 * mod:ore_sand_ore
 * mod:ore_dust_ore
 * mod:ore_chunk
 * mod:ore_crushed
 * mod:ore_powder
 * mod:ore_ingot
 */
class Ore(
        val color: Color,
        private val lazyIngot: LazyItemStack?
) : IForgeRegistryEntry.Impl<Ore>() {

    val ingot: ItemStack get() = lazyIngot?.get()?.copy() ?: ItemStack.EMPTY

    fun getNameForItem(type: ItemType) = ResourceLocation(ExNihilo.MODID, "ore_${registryName!!.resourcePath}_$type")
    fun getNameForBlock(type: BlockType) = ResourceLocation(ExNihilo.MODID, "ore_${registryName!!.resourcePath}_$type")

    fun hasItem(type: ItemType): Boolean {
        return true
    }

    fun hasBlock(type: BlockType): Boolean {
        return true
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): Ore? {
            val color = JsonHelper.deserializeColor(JsonUtils.getString(jsonObject, LibRegistries.COLOR, "ffffff"))

            val ingot = if (jsonObject.has(LibRegistries.ORE_INGOT)) {
                LazyItemStack.deserialize(jsonObject.getAsJsonObject(LibRegistries.ORE_INGOT))
            } else null

            if (jsonObject.has(LibRegistries.OREDICT_NAMES)) {

            }

            return Ore(color, ingot)
        }
    }
}
