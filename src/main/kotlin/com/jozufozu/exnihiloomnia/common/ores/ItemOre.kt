package com.jozufozu.exnihiloomnia.common.ores

import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import net.minecraft.item.Item

class ItemOre(val ore: Ore, val type: ItemType) : Item() {
    init {
        registryName = ore.getName(type)
        unlocalizedName = "exnihiloomnia.ores.${ore.getName(type).resourcePath}"
        creativeTab = OreManager.ORES_TAB
    }
}