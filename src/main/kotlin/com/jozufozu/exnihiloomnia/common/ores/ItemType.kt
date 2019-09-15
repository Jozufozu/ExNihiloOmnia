package com.jozufozu.exnihiloomnia.common.ores

enum class ItemType(val blockEquivalent: BlockType?) {
    CHUNK(BlockType.GRAVEL),
    NETHER_CHUNK(BlockType.NETHER_GRAVEL),
    END_CHUNK(BlockType.END_GRAVEL),
    CRUSHED(BlockType.SAND),
    POWDER(BlockType.DUST),
    INGOT(null);

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