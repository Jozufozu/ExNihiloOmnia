package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

open class ModItem(registryName: ResourceLocation, properties: Properties) : Item(properties.group(ExNihiloTabs.ITEMS)), Identifiable<Item> {
    init {
        this.registryName = registryName
    }

    override val identifier: ResourceLocation = registryName
}
