package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object ExNihiloTabs {
    val BARRELS: ItemGroup = object : ItemGroup("exnihiloomnia.barrels") {
        override fun createIcon(): ItemStack {
            return ItemStack(ExNihiloBlocks.BARREL_WOOD)
        }
    }

    val ITEMS: ItemGroup = object : ItemGroup("exnihiloomnia.items") {
        override fun createIcon(): ItemStack {
            return ItemStack(ExNihiloBlocks.SIEVE)
        }
    }
}
