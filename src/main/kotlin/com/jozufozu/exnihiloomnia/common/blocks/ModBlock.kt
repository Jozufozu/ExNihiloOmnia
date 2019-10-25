package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

open class ModBlock(override val identifier: ResourceLocation, properties: Properties) : Block(properties), Identifiable<Block> {
    init {
        this.registryName = registryName
    }

    override fun asItem(): Item {
        return super.asItem()
    }
}
