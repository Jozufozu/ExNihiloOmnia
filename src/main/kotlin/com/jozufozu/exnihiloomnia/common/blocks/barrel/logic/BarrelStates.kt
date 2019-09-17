package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.util.ResourceLocation

object BarrelStates {
    val ID_EMPTY = get("empty")
    val ID_COMPOST_COLLECT = get("compost_collect")
    val ID_COMPOSTING = get("composting")
    val ID_FLUID = get("fluid")
    val ID_ITEMS = get("items")

    val EMPTY: BarrelState get() {
            if (needsReload) init()
            return BarrelStateEmpty
        }
    val COMPOST_COLLECT: BarrelState get() {
            if (needsReload) init()
            return BarrelStateCompostCollect
        }
    val COMPOSTING: BarrelState get() {
            if (needsReload) init()
            return BarrelStateComposting
        }
    val FLUID: BarrelState get() {
            if (needsReload) init()
            return BarrelStateFluid
        }
    val ITEMS: BarrelState get() {
            if (needsReload) init()
            return BarrelStateItem
        }

    private val states = hashMapOf<ResourceLocation, BarrelState>()

    val STATES: HashMap<ResourceLocation, BarrelState> get() {
        if (needsReload) init()
        return states
    }

    private var needsReload = true

    fun setShouldReload() {
        needsReload = true
    }

    private fun init() {
        states.clear()

        register(BarrelStateEmpty)
        register(BarrelStateCompostCollect)
        register(BarrelStateComposting)
        register(BarrelStateFluid)
        register(BarrelStateItem)

        for (recipe in RegistryManager.FERMENTING) {
            val state = recipe.barrelState
            states[state.id] = state
        }

        for (recipe in RegistryManager.SUMMONING) {
            val state = recipe.barrelState
            states[state.id] = state
        }

        needsReload = false
    }

    fun getState(id: String) = states[ResourceLocation(id)]

    private fun get(name: String) = ResourceLocation(ExNihilo.MODID, name)
    private fun register(state: BarrelState) = state.also { states[it.id] = it }
}
