package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

open class ResourceLib {
    companion object {
        // Material prefixes
        const val WOOD = "wood"
        const val STONE = "stone"
        const val IRON = "iron"
        const val GOLD = "gold"
        const val DIAMOND = "diamond"

        const val PORCELAIN = "diamond"

        // Ex Nihilo materials
        const val SILK = "silk"
        const val BONE = "bone"

        // Tools
        const val MESH = "mesh"
        const val HAMMER = "hammer"
        const val CROOK = "crook"

        // Misc
        const val TREE_SEED = "seed"

        //Modifiers
        const val GLASS = "glass"
        const val CONCRETE = "concrete"
        const val TERRACOTTA = "terracotta"
        const val STAINED = "stained"
        const val GLAZED = "glazed"
        const val STRIPPED = "stripped"
        const val LOG = "log"

        //Blocks
        const val BARREL = "barrel"
        const val SIEVE = "sieve"

        const val BLACK = "black"
        const val RED = "red"
        const val GREEN = "green"
        const val BROWN = "brown"
        const val BLUE = "blue"
        const val PURPLE = "purple"
        const val CYAN = "cyan"
        const val LIGHT_GRAY = "light_gray"
        const val GRAY = "gray"
        const val PINK = "pink"
        const val LIME = "lime"
        const val YELLOW = "yellow"
        const val LIGHT_BLUE = "light_blue"
        const val MAGENTA = "magenta"
        const val ORANGE = "orange"
        const val WHITE = "white"

        const val OAK = "oak"
        const val SPRUCE = "spruce"
        const val BIRCH = "birch"
        const val JUNGLE = "jungle"
        const val ACACIA = "acacia"
        const val DARK_OAK = "dark_oak"

        fun get(vararg name: String) = ResourceLocation(ExNihilo.MODID, name.joinToString("_"))
    }
}