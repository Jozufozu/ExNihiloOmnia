package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.item.ItemStack
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry

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
class Ore : ForgeRegistryEntry<Ore>() {
    lateinit var color: Color
        private set

    /**
     * What all the blocks smelt into
     */
    lateinit var ingot: ItemStack
        private set

    companion object {
        @Throws(JsonParseException::class)
        fun deserialize(json: JsonObject): Ore {
            val out = Ore()

            out.color = Color.deserialize(JSONUtils.getString(json, RegistriesLib.COLOR, "ffffff"))

            if (json.has(RegistriesLib.ORE_INGOT)) {
                out.ingot = JsonHelper.deserializeItem(json.getAsJsonObject(RegistriesLib.ORE_INGOT), false)
            }

            return out
        }
    }
}
