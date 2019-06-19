package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.google.common.collect.HashBiMap
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.CollectCompostBarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.CompostingBarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.EmptyBarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.ItemBarrelState
import net.minecraft.util.ResourceLocation

object BarrelStates {
    private val STATES = HashBiMap.create<ResourceLocation, BarrelState>()

    val ID_EMPTY = get("empty")
    val ID_COLLECT_COMPOST = get("compost_collect")
    val ID_COMPOSTING = get("composting")
    val ID_ITEMS = get("items")
    //val ID_FLUID = get("fluid")

    var EMPTY: BarrelState = EmptyBarrelState().also { STATES[ID_EMPTY] = it }
    var COLLECT_COMPOST: BarrelState = CollectCompostBarrelState().also { STATES[ID_COLLECT_COMPOST] = it }
    var COMPOSTING: BarrelState = CompostingBarrelState().also { STATES[ID_COMPOSTING] = it }
    var ITEMS: BarrelState = ItemBarrelState().also { STATES[ID_ITEMS] = it }
    //var FLUID: BarrelState = FluidBarrelState().also { STATES[ID_FLUID] = it }

    operator fun get(name: ResourceLocation): BarrelState = STATES.getOrDefault(name, EMPTY)

    private fun get(name: String): ResourceLocation {
        return ResourceLocation(ExNihilo.MODID, name)
    }
}
