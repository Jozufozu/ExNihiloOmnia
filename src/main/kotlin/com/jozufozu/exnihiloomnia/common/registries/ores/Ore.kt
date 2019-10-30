package com.jozufozu.exnihiloomnia.common.registries.ores

import com.google.gson.*
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.ores.BlockType
import com.jozufozu.exnihiloomnia.common.ores.ItemType
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraft.item.ItemStack
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.ForgeRegistryEntry
import java.util.*
import kotlin.collections.ArrayList

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
        val smeltingExp: Float,
        private val variants: EnumSet<BlockType>,
        val hasGeneratedIngot: Boolean,
        val blockOreDict: Map<BlockType, List<String>>,
        val itemOreDict: Map<ItemType, List<String>>,
        private val lazyIngot: LazyItemStack?
) : ForgeRegistryEntry<Ore>() {
    val ingot: ItemStack get() = lazyIngot?.get()?.copy() ?: ItemStack.EMPTY

    fun getName(type: ItemType) = ResourceLocation(ExNihilo.MODID, "ore_${registryName!!.path}_$type")
    fun getName(type: BlockType) = ResourceLocation(ExNihilo.MODID, "ore_${registryName!!.path}_$type")

    fun getModel(type: ItemType) = ModelResourceLocation("${ExNihilo.MODID}:ores/item/ore_${registryName!!.namespace}_$type")
    fun getModel(type: BlockType) = ModelResourceLocation("${ExNihilo.MODID}:ores/block/ore_${registryName!!.namespace}_$type")

    fun hasItem(type: ItemType): Boolean {
        return if (type == ItemType.INGOT) hasGeneratedIngot
        else type.blockEquivalent?.let { variants.contains(it) } ?: false
    }

    fun hasBlock(type: BlockType): Boolean = variants.contains(type)

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(ore: JsonObject): Ore? {
            if (!CraftingHelper.processConditions(ore, LibRegistries.CONDITIONS)) return null

            val color = JsonHelper.deserializeColor(JSONUtils.getString(ore, LibRegistries.COLOR, "ffffff"))
            val smeltingExp = JSONUtils.getFloat(ore, "smeltingExp", 1f)

            val ingot = if (LibRegistries.ORE_INGOT in ore) {
                LazyItemStack.deserialize(ore.getAsJsonObject(LibRegistries.ORE_INGOT))
            } else null

            val itemNames = EnumMap<ItemType, MutableList<String>>(ItemType::class.java)
            val blockNames = EnumMap<BlockType, MutableList<String>>(BlockType::class.java)

            if (LibRegistries.OREDICT_NAMES in ore) {
                RegistryLoader.pushCtx(LibRegistries.OREDICT_NAMES)
                deserializeOredict(ore, blockNames, itemNames)
                RegistryLoader.popCtx()
            }

            val variants = if ("variants" in ore) {
                val set = EnumSet.noneOf(BlockType::class.java)
                when (val element = ore["variants"]) {
                    is JsonArray -> {
                        val ctx = RegistryLoader.pushCtx("variants")
                        element.forEachIndexed { i, entry ->
                            RegistryLoader.restoreCtx(ctx)
                            RegistryLoader.pushCtx(i.toString())
                            if (entry is JsonPrimitive && entry.isString) {
                                BlockType.deserialize(entry.asString)?.let(set::add) ?: RegistryLoader.warn("invalid ore block type: $entry")
                            } else {
                                RegistryLoader.warn("invalid ore block type, must be a string")
                            }
                        }
                        RegistryLoader.restoreCtx(ctx - 1)
                    }
                    else -> {
                        if (element is JsonPrimitive && element.isString) {
                            BlockType.deserialize(element.asString)?.let(set::add) ?: RegistryLoader.warn("invalid ore block type: $element")
                        } else {
                            RegistryLoader.warn("invalid ore block type, must be a string")
                        }
                    }
                }
                set
            } else EnumSet.of(BlockType.GRAVEL, BlockType.SAND, BlockType.DUST)

            return Ore(color, smeltingExp, variants, ingot == null, blockNames, itemNames, ingot)
        }

        private fun deserializeOredict(ore: JsonObject, blockNames: EnumMap<BlockType, MutableList<String>>, itemNames: EnumMap<ItemType, MutableList<String>>) {
            when (val oredict = ore[LibRegistries.OREDICT_NAMES]) {
                is JsonObject -> {
                    if ("blocks" in oredict) {
                        when (val blocks = oredict["blocks"]) {
                            is JsonObject -> {
                                for ((k, v) in blocks.entrySet()) {
                                    BlockType.deserialize(k)?.let {
                                        blockNames.getOrPut(it, { ArrayList() }).addAll(deserializeSingleOrArray(v))
                                    }
                                }
                            }
                            else -> {
                                val names = deserializeSingleOrArray(blocks)
                                for (type in BlockType.values()) {
                                    blockNames.getOrPut(type, { ArrayList() }).addAll(names)
                                }
                            }
                        }
                    }

                    if ("items" in oredict) {
                        when (val items = oredict["items"]) {
                            is JsonObject -> {
                                for ((k, v) in items.entrySet()) {
                                    ItemType.deserialize(k)?.let {
                                        itemNames.getOrPut(it, { ArrayList() }).addAll(deserializeSingleOrArray(v))
                                    }
                                }
                            }
                            else -> {
                                val names = deserializeSingleOrArray(items)
                                for (type in ItemType.values()) {
                                    itemNames.getOrPut(type, { ArrayList() }).addAll(names)
                                }
                            }
                        }
                    }
                }
                else -> {
                    val names = deserializeSingleOrArray(oredict)

                    for (name in names) {
                        val name = name.capitalize()
                        val block = "ore$name"
                        val ingot = "ingot$name"

                        BlockType.values().forEach { blockNames.getOrPut(it, { ArrayList() }).add(block) }
                        itemNames.getOrPut(ItemType.INGOT, { ArrayList() }).add(ingot)
                    }
                }
            }
        }

        private fun deserializeSingleOrArray(json: JsonElement): List<String> {
            val out = ArrayList<String>()
            when (json) {
                is JsonArray -> {
                    json.forEach {
                        if (it.isJsonPrimitive && it.asJsonPrimitive.isString) {
                            out.add(it.asString)
                        }
                    }
                }
                is JsonPrimitive -> {
                    if (json.isString) {
                        out.add(json.asString)
                    }
                }
            }
            return out
        }
    }
}
