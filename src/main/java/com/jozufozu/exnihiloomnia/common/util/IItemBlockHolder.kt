package com.jozufozu.exnihiloomnia.common.util

import net.minecraft.item.ItemBlock

interface IItemBlockHolder {
    /**
     * Convenience method for registering ItemBlocks
     * @return A new [ItemBlock] instance if items have not been registered yet, or the block's item if it has
     */
    val itemBlock: ItemBlock
}
