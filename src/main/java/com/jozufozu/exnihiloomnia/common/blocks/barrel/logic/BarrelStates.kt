package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.google.common.collect.HashBiMap
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

    val EMPTY: BarrelState = BarrelStateEmpty()
    val COMPOST_COLLECT: BarrelState = BarrelStateCompostCollect()
    val COMPOSTING: BarrelState = BarrelStateComposting()
    val FLUID: BarrelState = BarrelStateItem()
    val ITEMS: BarrelState = BarrelStateFluid()

    private fun get(name: String): ResourceLocation = ResourceLocation(ExNihilo.MODID, name)
}
