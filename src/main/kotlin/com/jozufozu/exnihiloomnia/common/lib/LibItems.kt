package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object LibItems {
    /**
     * Material prefixes
     */
    const val WOOD = "wooden"
    const val STONE = "stone"
    const val IRON = "iron"
    const val GOLD = "gold"
    const val DIAMOND = "diamond"

    const val PORCELAIN = "porcelain"

    /**
     * Ex Nihilo materials
     */
    const val SILK = "silk"
    const val BONE = "bone"

    /**
     * Tools
     */
    const val MESH = "mesh"
    const val HAMMER = "hammer"
    const val CROOK = "crook"

    /**
     * Mesh
     */
    val WOODEN_MESH = get(WOOD, MESH)
    val SILK_MESH = get(SILK, MESH)
    val GOLD_MESH = get(GOLD, MESH)
    val DIAMOND_MESH = get(DIAMOND, MESH)

    val WOODEN_CROOK = get(WOOD, CROOK)
    val BONE_CROOK = get(BONE, CROOK)

    val WOODEN_HAMMER = get(WOOD, HAMMER)
    val STONE_HAMMER = get(STONE, HAMMER)
    val IRON_HAMMER = get(IRON, HAMMER)
    val GOLD_HAMMER = get(GOLD, HAMMER)
    val DIAMOND_HAMMER = get(DIAMOND, HAMMER)

    val TREE_SEED = get("tree_seed")
    val SMALL_STONE = get("small_stone")

    val ASTROLABE = get("astrolabe")
    val ASH = get("ash")
    val SILKWORM = get("silkworm")

    val PORCELAIN_CLAY = get(PORCELAIN, "clay")

    val PORCELAIN_BUCKET = get(PORCELAIN, "bucket")

    private fun get(vararg name: String): ResourceLocation {
        return ResourceLocation(ExNihilo.MODID, name.joinToString("_"))
    }
}