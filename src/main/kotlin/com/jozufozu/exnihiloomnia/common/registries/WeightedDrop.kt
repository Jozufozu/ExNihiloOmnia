package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.util.IRewardProcessor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import java.util.*

class WeightedDrop(
        val drop: ItemStack,
        val chance: Float = 0.0f,
        val type: String = ""
) {

    fun roll(user: EntityPlayer, activeStack: ItemStack, random: Random): ItemStack {
        var chance = this.chance
        if (activeStack.item is IRewardProcessor)
            chance *= (activeStack.item as IRewardProcessor).getEffectivenessForType(this.type)

        return if (random.nextFloat() < chance) drop.copy() else ItemStack.EMPTY
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(drop: JsonElement): WeightedDrop {
            if (drop is JsonObject) {
                val item = JsonHelper.deserializeItem(drop, true)
                val chance = JsonUtils.getFloat(drop, LibRegistries.CHANCE, 1.0f)
                val type = JsonUtils.getString(drop, LibRegistries.DROP_CATEGORY, "")

                return WeightedDrop(item, chance, type)
            } else if (drop.isJsonPrimitive && drop.asJsonPrimitive.isString) {
                val itemName = drop.asJsonPrimitive.asString
                val itemMaybe = Item.REGISTRY.getObject(ResourceLocation(itemName))
                        ?: throw JsonParseException("unknown item '$itemName'")

                return WeightedDrop(ItemStack(itemMaybe), 1f)
            } else {
                throw JsonParseException("reward must be either a string or an object")
            }
        }
    }
}
