package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object LibBlocks {
    //Modifiers
    const val SUFFIX_WOOD = "_wood"
    const val SUFFIX_STONE = "_stone"
    const val SUFFIX_GLASS = "_glass"
    const val SUFFIX_CONCRETE = "_concrete"
    const val SUFFIX_TERRACOTTA = "_terracotta"
    const val STAINED = "_stained"

    //Blocks
    const val BARREL = "barrel"

    val SIEVE = get("sieve")
    val WOODEN_BARREL = get(BARREL, SUFFIX_WOOD)
    val STONE_BARREL = get(BARREL, SUFFIX_STONE)
    val GLASS_BARREL = get(BARREL, SUFFIX_GLASS)
    val STAINED_GLASS_BARREL = get(BARREL, STAINED, SUFFIX_GLASS)
    val TERRACOTTA_BARREL = get(BARREL, SUFFIX_TERRACOTTA)
    val CONCRETE_BARREL = get(BARREL, SUFFIX_CONCRETE)

    val CRUCIBLE = get("crucible")
    val RAW_CRUCIBLE = get("raw_crucible")

    val DUST = get("dust")
    val NETHER_GRAVEL = get("nether_gravel")
    val END_GRAVEL = get("end_gravel")

    val INFESTED_LEAVES = get("infested_leaves")
    val WITCHWATER = get("witchwater")

    private operator fun get(vararg name: String): ResourceLocation {
        val builder = StringBuilder()
        for (s in name) {
            builder.append(s)
        }

        return ResourceLocation(ExNihilo.MODID, builder.toString())
    }
}
