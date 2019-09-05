package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object LibItems {
    /**
     * Material prefixes
     */
    const val WOOD = "_wood"
    const val STONE = "_stone"
    const val IRON = "_iron"
    const val GOLD = "_gold"
    const val DIAMOND = "_diamond"

    /**
     * Ex Nihilo materials
     */
    const val SILK = "_silk"
    const val BONE = "_bone"

    /**
     * Tools
     */
    const val MESH = "mesh"
    const val HAMMER = "hammer"

    /**
     * Mesh
     */
    val WOODEN_MESH = get(MESH, WOOD)
    val SILK_MESH = get(MESH, SILK)
    val GOLD_MESH = get(MESH, GOLD)
    val DIAMOND_MESH = get(MESH, DIAMOND)

    val CROOK = get("crook")
    val BONE_CROOK = get("crook_bone")

    val WOODEN_HAMMER = get(HAMMER, WOOD)
    val STONE_HAMMER = get(HAMMER, STONE)
    val IRON_HAMMER = get(HAMMER, IRON)
    val GOLD_HAMMER = get(HAMMER, GOLD)
    val DIAMOND_HAMMER = get(HAMMER, DIAMOND)

    val TREE_SEED = get("tree_seed")
    val SMALL_STONE = get("small_stone")

    val ASTROLABE = get("astrolabe")
    val ASH = get("ash")
    val SILKWORM = get("silkworm")

    private operator fun get(vararg name: String): ResourceLocation {
        val builder = StringBuilder()
        for (s in name) {
            builder.append(s)
        }

        return ResourceLocation(ExNihilo.MODID, builder.toString())
    }
}
