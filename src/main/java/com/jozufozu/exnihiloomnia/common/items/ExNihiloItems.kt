package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.tools.HammerItem
import com.jozufozu.exnihiloomnia.common.items.tools.MeshItem
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.util.registry.Registry
import net.minecraftforge.event.RegistryEvent

import java.util.ArrayList

object ExNihiloItems {
    val modItems = ArrayList<Identifiable<Item>>()

    val STONE: Item = SmallStoneItem()
    val ASTROLABE: Item = ItemJadeAstrolabe()
    val ASH: Item = AshItem()

    fun registerItems(event: RegistryEvent.Register<Item>) {
        for (item in modItems) {
            event.registry.register(item.getIdentified())
        }

        for (block in ExNihiloBlocks.modBlocks)
            event.registry.register(block.asItem())
    }
}