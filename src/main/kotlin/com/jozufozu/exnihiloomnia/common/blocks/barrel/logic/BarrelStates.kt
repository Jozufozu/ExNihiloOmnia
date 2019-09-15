package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.*
import net.minecraft.util.ResourceLocation

object BarrelStates {
    val STATES: HashMap<ResourceLocation, BarrelState> = HashMap()

    val ID_EMPTY = get("empty")
    val ID_COMPOST_COLLECT = get("compost_collect")
    val ID_COMPOSTING = get("composting")
    val ID_FLUID = get("fluid")
    val ID_ITEMS = get("items")

    val EMPTY = BarrelStateEmpty()
    val COMPOST_COLLECT = BarrelStateCompostCollect()
    val COMPOSTING = BarrelStateComposting()
    val FLUID = BarrelStateFluid()
    val ITEMS = BarrelStateItem()

    init {
        init()
    }

    fun init() {
        STATES.clear()

        register(EMPTY)
        register(COMPOST_COLLECT)
        register(COMPOSTING)
        register(FLUID)
        register(ITEMS)
    }

    fun getState(id: String) = STATES[ResourceLocation(id)]

    private fun get(name: String) = ResourceLocation(ExNihilo.MODID, name)
    private fun register(state: BarrelState) = state.also { STATES[it.id] = it }
}
