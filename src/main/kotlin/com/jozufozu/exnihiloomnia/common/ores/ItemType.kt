package com.jozufozu.exnihiloomnia.common.ores

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

enum class ItemType(val blockEquivalent: BlockType?, val siftedFrom: Lazy<ItemStack?>) {
    CHUNK(BlockType.GRAVEL, lazy { ItemStack(Blocks.GRAVEL) }),
    NETHER_CHUNK(BlockType.NETHER_GRAVEL, lazy { ItemStack(ExNihiloBlocks.NETHER_GRAVEL) }),
    END_CHUNK(BlockType.END_GRAVEL, lazy { ItemStack(ExNihiloBlocks.END_GRAVEL) }),
    CRUSHED(BlockType.SAND, lazy { ItemStack(Blocks.SAND) }),
    POWDER(BlockType.DUST, lazy { ItemStack(ExNihiloBlocks.DUST) }),
    INGOT(null, lazy { null });

    override fun toString(): String = when (this) {
        CHUNK -> "chunk"
        NETHER_CHUNK -> "nether_chunk"
        END_CHUNK -> "end_chunk"
        CRUSHED -> "crushed"
        POWDER -> "powder"
        INGOT -> "ingot"
    }

    companion object {
        fun deserialize(string: String): ItemType? {
            return when (string) {
                "chunk" -> CHUNK
                "netherChunk" -> NETHER_CHUNK
                "endChunk" -> END_CHUNK
                "crushed" -> CRUSHED
                "powder" -> POWDER
                "ingot" -> INGOT
                else -> null
            }
        }
    }
}