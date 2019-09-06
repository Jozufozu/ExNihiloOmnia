package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonArray
import com.google.gson.JsonParseException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import java.util.*


class WeightedRewards {
    private val _outputs = ArrayList<WeightedDrop>()

    val outputs: List<WeightedDrop> get() = _outputs

    fun addOutput(output: WeightedDrop) {
        _outputs.add(output)
    }

    /**
     * Gets a list of random drops based on the outputs
     * @param player
     * @param processor The item being used to generate these rewards
     * @param random
     */
    fun roll(player: EntityPlayer?, processor: ItemStack, random: Random): NonNullList<ItemStack> {
        val out = NonNullList.create<ItemStack>()

        for (reward in outputs) {
            val roll = reward.roll(player, processor, random)

            if (roll.isEmpty)
                continue

            if (roll.count > 1) {
                while (roll.count > 0) {
                    out.add(ItemStack(roll.item, 1, roll.metadata).also { it.tagCompound = roll.tagCompound?.copy() })
                    roll.shrink(1)
                }
            } else out.add(roll)
        }

        return out
    }

    companion object {

        @Throws(JsonParseException::class)
        fun deserialize(array: JsonArray?): WeightedRewards {
            if (array == null) throw JsonParseException("recipe is missing rewards")

            val out = WeightedRewards()

            for ((i, drop) in array.withIndex()) {
                RegistryLoader.pushCtx(i.toString())
                try {
                    out.addOutput(WeightedDrop.deserialize(drop))
                } catch (e: Exception) {
                    RegistryLoader.error(e)
                } finally {
                    RegistryLoader.popCtx()
                }
            }

            if (out.outputs.isEmpty()) throw JsonParseException("recipe has empty rewards set")

            return out
        }
    }
}
