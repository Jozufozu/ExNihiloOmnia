package com.jozufozu.exnihiloomnia.common.registries

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.gson.JsonArray
import com.google.gson.JsonParseException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemHandlerHelper
import java.util.*


class WeightedRewards {
    private val outputs = ArrayList<WeightedDrop>()

    val equivalencies: HashMap<ItemStack, List<WeightedDrop>>
        get() {
            val temp = Maps.newHashMap<ItemStack, MutableList<WeightedDrop>>()

            output@ for (reward in outputs) {
                val drop = reward.drop

                if (drop == ItemStack.EMPTY)
                    continue

                for ((itemStack, entries) in temp.entries) {
                    if (itemStack.isItemEqual(drop)) {
                        entries.add(reward)
                        continue@output
                    }
                }

                temp[ItemHandlerHelper.copyStackWithSize(drop, 1)] = Lists.newArrayList(reward)
            }

            val sorted = ArrayList(temp.keys)
            sorted.sortWith(Comparator.comparingInt { o -> Item.REGISTRY.getIDForObject(o.item) })

            val out = Maps.newHashMap<ItemStack, List<WeightedDrop>>()

            for (stack in sorted) {
                out[stack] = temp[stack]
            }

            return out
        }

    fun addOutput(output: WeightedDrop) {
        outputs.add(output)
    }

    /**
     * Gets a list of random drops based on the outputs
     * @param player
     * @param processor The item being used to generate these rewards
     * @param random
     */
    fun roll(player: EntityPlayer?, processor: ItemStack, random: Random): NonNullList<ItemStack> {
        val ret = NonNullList.create<ItemStack>()

        val temp = NonNullList.create<ItemStack>()

        for (reward in outputs) {
            val roll = reward.roll(player!!, processor, random)

            if (roll.isEmpty)
                continue

            temp.add(roll)
        }

        for (stack in temp) {
            if (stack.count > 1) {
                while (stack.count > 0) {
                    ret.add(ItemStack(stack.item, 1, stack.metadata))
                    stack.shrink(1)
                }
            } else {
                ret.add(stack)
            }
        }

        return ret
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

            if (out.outputs.size == 0) throw JsonParseException("recipe has empty rewards set")

            return out
        }
    }
}
