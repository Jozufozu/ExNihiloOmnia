package com.jozufozu.exnihiloomnia.common.util;

import net.minecraft.item.ItemBlock;

public interface IItemBlockHolder
{
    /**
     * Convenience method for registering ItemBlocks
     * @return A new {@link ItemBlock} instance if items have not been registered yet, or the block's item if it has
     */
    ItemBlock getItemBlock();
}
