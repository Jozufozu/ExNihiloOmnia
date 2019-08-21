package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier

open class ExNihiloBlock(override val identifier: Identifier, properties: Settings) : Block(properties), Identifiable<Block> {
    protected var item: Item? = null

    override fun asItem(): Item {
        if (item == null) {
            item = BlockItem(this, Item.Settings().group(ExNihiloTabs.BLOCKS))
        }

        return item!!
    }

    override fun getIdentified() = this
}
