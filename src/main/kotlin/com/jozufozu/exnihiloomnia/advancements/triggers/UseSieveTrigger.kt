package com.jozufozu.exnihiloomnia.advancements.triggers

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.advancements.ICriterionTrigger
import net.minecraft.advancements.PlayerAdvancements
import net.minecraft.advancements.critereon.AbstractCriterionInstance
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation

class UseSieveTrigger : ICriterionTrigger<UseSieveTrigger.Instance> {
    private val listeners = Maps.newHashMap<PlayerAdvancements, Listeners>()


    override fun getId(): ResourceLocation {
        return ID
    }

    override fun addListener(playerAdvancementsIn: PlayerAdvancements, listener: ICriterionTrigger.Listener<Instance>) {
        val sieveListener = this.listeners.computeIfAbsent(playerAdvancementsIn, ::Listeners)

        sieveListener.add(listener)
    }

    override fun removeListener(playerAdvancementsIn: PlayerAdvancements, listener: ICriterionTrigger.Listener<Instance>) {
        val sieveListener = this.listeners.computeIfAbsent(playerAdvancementsIn, ::Listeners)

        sieveListener.remove(listener)
    }

    override fun removeAllListeners(playerAdvancementsIn: PlayerAdvancements) {
        this.listeners.remove(playerAdvancementsIn)
    }

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance {
        var recipe: ResourceLocation? = null

        if (json.has("recipe")) {
            recipe = ResourceLocation(JsonUtils.getString(json, "recipe"))
        }

        val drops = ItemPredicate.deserializeArray(json)

        return Instance(recipe, drops)
    }

    fun trigger(player: EntityPlayerMP, sifted: ResourceLocation, drops: NonNullList<ItemStack>) {
        val listeners = this.listeners[player.advancements]

        listeners?.trigger(sifted, drops)
    }

    class Instance(private val recipe: ResourceLocation?, private val drops: Array<ItemPredicate>?) : AbstractCriterionInstance(ID) {

        fun test(recipeName: ResourceLocation, drops: NonNullList<ItemStack>): Boolean {
            val list = Lists.newArrayList(*this.drops ?: arrayOfNulls(0))

            for (drop in drops) {
                list.removeIf { itemPredicate -> itemPredicate.test(drop) }
            }

            return list.isEmpty() && this.recipe == null || recipeName == this.recipe
        }
    }

    class Listeners(private val playerAdvancements: PlayerAdvancements) {
        private val listeners = Sets.newHashSet<ICriterionTrigger.Listener<UseSieveTrigger.Instance>>()

        val isEmpty: Boolean
            get() = this.listeners.isEmpty()

        fun add(listener: ICriterionTrigger.Listener<UseSieveTrigger.Instance>) {
            this.listeners.add(listener)
        }

        fun remove(listener: ICriterionTrigger.Listener<UseSieveTrigger.Instance>) {
            this.listeners.remove(listener)
        }

        fun trigger(recipeName: ResourceLocation, drops: NonNullList<ItemStack>) {
            var list: MutableList<ICriterionTrigger.Listener<UseSieveTrigger.Instance>>? = null

            for (listener in this.listeners) {
                if (listener.criterionInstance.test(recipeName, drops)) {
                    if (list == null) {
                        list = Lists.newArrayList<ICriterionTrigger.Listener<Instance>>()
                    }

                    list!!.add(listener)
                }
            }

            if (list != null) {
                for (listener in list) {
                    listener.grantCriterion(this.playerAdvancements)
                }
            }
        }
    }

    companion object {
        val ID = ResourceLocation(ExNihilo.MODID, "sieve_item")
    }
}
