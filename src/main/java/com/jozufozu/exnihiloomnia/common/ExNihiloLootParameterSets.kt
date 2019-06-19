package com.jozufozu.exnihiloomnia.common

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation
import net.minecraft.world.storage.loot.LootParameterSet
import net.minecraft.world.storage.loot.LootParameters

object ExNihiloLootParameterSets {
    @JvmField val SIEVING = register("sieving") {
        it      .required(LootParameters.POSITION)
                .optional(LootParameters.THIS_ENTITY)
    }

    @JvmField val HAMMERING = register("hammering") {
        it      .required(LootParameters.POSITION)
        it      .required(LootParameters.TOOL)
                .optional(LootParameters.THIS_ENTITY)
    }

    private fun register(name: String, builderConsumer: (LootParameterSet.Builder) -> Unit): LootParameterSet {
        val builder = LootParameterSet.Builder()
        builderConsumer(builder)
        val lootparameterset = builder.build()
        val resourcelocation = ResourceLocation(ExNihilo.MODID, name)
        //val lootparameterset1 = REGISTRY.put(resourcelocation, lootparameterset)
        return if (lootparameterset1 != null) {
            throw IllegalStateException("Loot table parameter set $resourcelocation is already registered")
        } else {
            lootparameterset
        }
    }
}
