package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object ExNihiloTabs {
    val BARRELS: CreativeTabs = object : CreativeTabs("exnihiloomnia.barrels") {
        override fun getTabIconItem(): ItemStack {
            return ItemStack(ExNihiloBlocks.BARREL_WOOD)
        }
    }

    val ITEMS: CreativeTabs = object : CreativeTabs("exnihiloomnia.items") {
        override fun getTabIconItem(): ItemStack {
            return ItemStack(ExNihiloBlocks.SIEVE)
        }
    }
}
