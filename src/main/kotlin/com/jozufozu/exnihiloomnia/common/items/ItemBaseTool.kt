package com.jozufozu.exnihiloomnia.common.items

import com.google.common.collect.Sets
import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.item.IItemTier
import net.minecraft.item.Item
import net.minecraft.item.ToolItem
import net.minecraft.util.ResourceLocation

open class ItemBaseTool(final override val identifier: ResourceLocation, toolMaterial: IItemTier, properties: Properties, damage: Float = 0f, attackSpeed: Float = 0f, effectiveBlocksIn: Set<Block> = Sets.newHashSet()) : ToolItem(damage, attackSpeed, toolMaterial, effectiveBlocksIn, properties.group(ExNihiloTabs.ITEMS)), Identifiable<Item> {

    init {
        this.registryName = identifier
    }
}
