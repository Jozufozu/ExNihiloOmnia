package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.util.IRewardProcessor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import java.util.*

class WeightedDrop {
    lateinit var drop: ItemStack
        private set
    var chance: Float = 0.0f
        private set

    lateinit var type: String
        private set

    constructor()

    constructor(drop: ItemStack, chance: Float) {
        this.drop = drop
        this.chance = chance
    }

    fun roll(user: EntityPlayer, activeStack: ItemStack, random: Random): ItemStack {
        var chance = this.chance
        if (activeStack.item is IRewardProcessor)
            chance *= (activeStack.item as IRewardProcessor).getEffectivenessForType(this.type)

        return if (random.nextFloat() < chance) drop.copy() else ItemStack.EMPTY
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(`object`: JsonObject): WeightedDrop {
            val out = WeightedDrop()

            out.drop = JsonHelper.deserializeItem(`object`, true)
            out.chance = JsonUtils.getFloat(`object`, LibRegistries.CHANCE, 1.0f)
            out.type = JsonUtils.getString(`object`, LibRegistries.DROP_CATEGORY, "")

            return out
        }
    }
}
