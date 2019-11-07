package com.jozufozu.exnihiloomnia.common

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.Tag
import net.minecraft.util.ResourceLocation

object ExNihiloTags {
    object Blocks {
        val THROWN_STONE_BREAKING = tag("thrown_stone_breaking")

        private fun tag(name: String): Tag<Block> = BlockTags.Wrapper(ResourceLocation(ExNihilo.MODID, name))
    }

    object Items {
        private fun tag(name: String): Tag<Item> = ItemTags.Wrapper(ResourceLocation(ExNihilo.MODID, name))
    }
}