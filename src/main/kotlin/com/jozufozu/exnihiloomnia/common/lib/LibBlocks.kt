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

    @JvmField val SIEVE = get("sieve")
    @JvmField val WOODEN_BARREL = get(BARREL, SUFFIX_WOOD)
    @JvmField val STONE_BARREL = get(BARREL, SUFFIX_STONE)
    @JvmField val GLASS_BARREL = get(BARREL, SUFFIX_GLASS)
    @JvmField val STAINED_GLASS_BARREL = get(BARREL, STAINED, SUFFIX_GLASS)
    @JvmField val TERRACOTTA_BARREL = get(BARREL, SUFFIX_TERRACOTTA)
    @JvmField val CONCRETE_BARREL = get(BARREL, SUFFIX_CONCRETE)

    @JvmField val CRUCIBLE = get("crucible")
    @JvmField val RAW_CRUCIBLE = get("raw_crucible")

    @JvmField val DUST = get("dust")
    @JvmField val NETHER_GRAVEL = get("nether_gravel")
    @JvmField val END_GRAVEL = get("end_gravel")

    @JvmField val INFESTED_LEAVES = get("infested_leaves")

    private operator fun get(vararg name: String): ResourceLocation {
        val builder = StringBuilder()
        for (s in name) {
            builder.append(s)
        }

        return ResourceLocation(ExNihilo.MODID, builder.toString())
    }
}
