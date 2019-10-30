package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import java.util.*

class WeightedDrop(
        val drop: ItemStack,
        val chance: Float = 0.0f,
        val type: String = ""
) {

    fun roll(user: PlayerEntity?, multipliers: Map<String, Float>, random: Random): ItemStack {
        var chance = this.chance
        user?.let { chance += it.luck }
        chance *= multipliers.getOrDefault(type, 1f)

        return if (random.nextFloat() < chance) drop.copy() else ItemStack.EMPTY
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(drop: JsonElement): WeightedDrop {
            return when {
                drop is JsonObject -> {
                    val item = JsonHelper.deserializeItem(drop, true)
                    val chance = JSONUtils.getFloat(drop, LibRegistries.CHANCE, 1.0f)
                    val type = JSONUtils.getString(drop, LibRegistries.DROP_CATEGORY, "")

                    WeightedDrop(item, chance, type)
                }
                drop.isJsonPrimitive && drop.asJsonPrimitive.isString -> {
                    val itemName = drop.asJsonPrimitive.asString
                    val itemMaybe = ForgeRegistries.ITEMS.getValue(ResourceLocation(itemName))
                            ?: throw JsonParseException("unknown item '$itemName'")

                    WeightedDrop(ItemStack(itemMaybe), 1f)
                }
                else -> throw JsonSyntaxException("reward must be either a string or an object")
            }
        }
    }
}
