package com.jozufozu.exnihiloomnia.common.ores

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

enum class BlockType(val itemEquivalent: ItemType, val material: Material, val soundType: SoundType) {
    GRAVEL(ItemType.CHUNK, Material.GROUND, SoundType.GROUND),
    NETHER_GRAVEL(ItemType.NETHER_CHUNK, Material.GROUND, SoundType.GROUND),
    END_GRAVEL(ItemType.END_CHUNK, Material.GROUND, SoundType.GROUND),
    SAND(ItemType.CRUSHED, Material.SAND, SoundType.SAND),
    DUST(ItemType.POWDER, Material.SAND, SoundType.SNOW);

    override fun toString(): String = when (this) {
        GRAVEL -> "gravel"
        NETHER_GRAVEL -> "nether_gravel"
        END_GRAVEL -> "end_gravel"
        SAND -> "sand"
        DUST -> "dust"
    }

    companion object {
        fun deserialize(string: String): BlockType? {
            return when (string) {
                "gravel" -> GRAVEL
                "netherGravel" -> NETHER_GRAVEL
                "endGravel" -> END_GRAVEL
                "sand" -> SAND
                "dust" -> DUST
                else -> null
            }
        }
    }
}