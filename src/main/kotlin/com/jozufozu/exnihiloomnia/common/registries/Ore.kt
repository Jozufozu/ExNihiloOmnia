package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.*

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
class Ore : IForgeRegistryEntry.Impl<Ore>() {
    private lateinit var color: Color

    /**
     * The chance that each type of item will drop
     * If negative, the reward will not be added
     */
    private var gravel_chance = -1f
    private var sand_chance = -1f
    private var dust_chance = -1f

    /**
     * The blocks are assigned %sOre
     * The ingot is assigned %sIngot, if an ingot is generated
     */
    private var oreDictNames: ArrayList<String> = ArrayList()

    /**
     * What all the blocks smelt into
     */
    private lateinit var ingot: ItemStack

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(jsonObject: JsonObject): Ore {
            val out = Ore()

            out.color = JsonHelper.deserializeColor(JsonUtils.getString(jsonObject, LibRegistries.COLOR, "ffffff"))

            val chance = JsonUtils.getFloat(jsonObject, LibRegistries.CHANCE, -1f)

            out.gravel_chance = JsonUtils.getFloat(jsonObject, LibRegistries.GRAVEL_CHANCE, chance)
            out.sand_chance = JsonUtils.getFloat(jsonObject, LibRegistries.SAND_CHANCE, chance)
            out.dust_chance = JsonUtils.getFloat(jsonObject, LibRegistries.DUST_CHANCE, chance)

            if (jsonObject.has(LibRegistries.ORE_INGOT)) {
                out.ingot = JsonHelper.deserializeItem(jsonObject.getAsJsonObject(LibRegistries.ORE_INGOT), false)
            }

            if (jsonObject.has(LibRegistries.OREDICT_NAMES)) {
                out.oreDictNames = ArrayList()

                for (jsonElement in jsonObject.getAsJsonArray(LibRegistries.OREDICT_NAMES)) {
                    out.oreDictNames.add(jsonElement.asJsonPrimitive.asString)
                }
            }

            return out
        }
    }
}
