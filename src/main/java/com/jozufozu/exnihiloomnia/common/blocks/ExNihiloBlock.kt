package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

open class ExNihiloBlock(registryName: ResourceLocation, properties: Properties) : Block(properties) {
    init {
        this.registryName = registryName
    }

    protected var item: Item? = null

    override fun asItem(): Item {
        if (item == null) {
            item = BlockItem(this, Item.Properties().group(ExNihiloTabs.BLOCKS))
        }

        return item!!
    }
}
