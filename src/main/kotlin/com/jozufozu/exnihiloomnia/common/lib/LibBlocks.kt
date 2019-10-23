package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object LibBlocks {
    //Modifiers
    const val WOOD = "wood"
    const val STONE = "stone"
    const val GLASS = "glass"
    const val CONCRETE = "concrete"
    const val TERRACOTTA = "terracotta"
    const val STAINED = "stained"

    //Blocks
    const val BARREL = "barrel"

    val SIEVE = get("sieve")
    val WOODEN_BARREL = get(BARREL, WOOD)
    val STONE_BARREL = get(BARREL, STONE)
    val GLASS_BARREL = get(BARREL, GLASS)
    val STAINED_GLASS_BARREL = get(BARREL, STAINED, GLASS)
    val TERRACOTTA_BARREL = get(BARREL, TERRACOTTA)
    val CONCRETE_BARREL = get(BARREL, CONCRETE)

    val CRUCIBLE = get("crucible")
    val RAW_CRUCIBLE = get("raw_crucible")

    val DUST = get("dust")
    val NETHER_GRAVEL = get("nether_gravel")
    val END_GRAVEL = get("end_gravel")

    val INFESTED_LEAVES = get("infested_leaves")
    val WITCHWATER = get("witchwater")

    private fun get(vararg name: String): ResourceLocation {
        return ResourceLocation(ExNihilo.MODID, name.joinToString("_"))
    }
}
