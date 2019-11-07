
package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

open class ModItem(final override val identifier: ResourceLocation, properties: Properties = Properties()) : Item(properties.group(ExNihiloTabs.ITEMS)), Identifiable<Item> {
    init {
        this.registryName = identifier
    }
}