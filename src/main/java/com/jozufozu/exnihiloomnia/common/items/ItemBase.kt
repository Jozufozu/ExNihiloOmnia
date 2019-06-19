package com.jozufozu.exnihiloomnia.common.items

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

open class ItemBase(registryName: ResourceLocation, properties: Properties) : Item(properties.group(ExNihiloTabs.ITEMS)) {
    init {
        this.registryName = registryName
    }
}
