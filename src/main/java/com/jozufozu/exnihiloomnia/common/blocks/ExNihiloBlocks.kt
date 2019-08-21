package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelBlock
import com.jozufozu.exnihiloomnia.common.blocks.barrel.GlassBarrelBlock
import com.jozufozu.exnihiloomnia.common.blocks.crucible.CrucibleBlock
import com.jozufozu.exnihiloomnia.common.blocks.crucible.RawCrucibleBlock
import com.jozufozu.exnihiloomnia.common.blocks.sieve.SieveBlock
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraftforge.event.RegistryEvent
import java.util.*

object ExNihiloBlocks {
    var modBlocks = ArrayList<Block>()

    val GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.GLASS_BARREL, Block.Settings.copy(Blocks.GLASS)))
    val STONE_BARREL = register(BarrelBlock(BlocksLib.STONE_BARREL, Block.Settings.copy(Blocks.STONE)))

    val OAK_WOOD_BARREL = register(BarrelBlock(BlocksLib.OAK_WOOD_BARREL, Block.Settings.copy(Blocks.OAK_WOOD)))
    val SPRUCE_WOOD_BARREL = register(BarrelBlock(BlocksLib.SPRUCE_WOOD_BARREL, Block.Settings.copy(Blocks.SPRUCE_WOOD)))
    val BIRCH_WOOD_BARREL = register(BarrelBlock(BlocksLib.BIRCH_WOOD_BARREL, Block.Settings.copy(Blocks.BIRCH_WOOD)))
    val JUNGLE_WOOD_BARREL = register(BarrelBlock(BlocksLib.JUNGLE_WOOD_BARREL, Block.Settings.copy(Blocks.JUNGLE_WOOD)))
    val ACACIA_WOOD_BARREL = register(BarrelBlock(BlocksLib.ACACIA_WOOD_BARREL, Block.Settings.copy(Blocks.ACACIA_WOOD)))
    val DARK_OAK_WOOD_BARREL = register(BarrelBlock(BlocksLib.DARK_OAK_WOOD_BARREL, Block.Settings.copy(Blocks.DARK_OAK_WOOD)))

    val OAK_LOG_BARREL = register(BarrelBlock(BlocksLib.OAK_LOG_BARREL, Block.Settings.copy(Blocks.OAK_LOG)))
    val SPRUCE_LOG_BARREL = register(BarrelBlock(BlocksLib.SPRUCE_LOG_BARREL, Block.Settings.copy(Blocks.SPRUCE_LOG)))
    val BIRCH_LOG_BARREL = register(BarrelBlock(BlocksLib.BIRCH_LOG_BARREL, Block.Settings.copy(Blocks.BIRCH_LOG)))
    val JUNGLE_LOG_BARREL = register(BarrelBlock(BlocksLib.JUNGLE_LOG_BARREL, Block.Settings.copy(Blocks.JUNGLE_LOG)))
    val ACACIA_LOG_BARREL = register(BarrelBlock(BlocksLib.ACACIA_LOG_BARREL, Block.Settings.copy(Blocks.ACACIA_LOG)))
    val DARK_OAK_LOG_BARREL = register(BarrelBlock(BlocksLib.DARK_OAK_LOG_BARREL, Block.Settings.copy(Blocks.DARK_OAK_LOG)))

    val STRIPPED_OAK_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_OAK_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_OAK_LOG)))
    val STRIPPED_SPRUCE_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_SPRUCE_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_SPRUCE_LOG)))
    val STRIPPED_BIRCH_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_BIRCH_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_BIRCH_LOG)))
    val STRIPPED_JUNGLE_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_JUNGLE_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_JUNGLE_LOG)))
    val STRIPPED_ACACIA_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_ACACIA_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_ACACIA_LOG)))
    val STRIPPED_DARK_OAK_LOG_BARREL = register(BarrelBlock(BlocksLib.STRIPPED_DARK_OAK_LOG_BARREL, Block.Settings.copy(Blocks.STRIPPED_DARK_OAK_LOG)))

    val BLACK_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.BLACK_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.BLACK_STAINED_GLASS)))
    val RED_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.RED_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.RED_STAINED_GLASS)))
    val GREEN_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.GREEN_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.GREEN_STAINED_GLASS)))
    val BROWN_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.BROWN_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.BROWN_STAINED_GLASS)))
    val BLUE_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.BLUE_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.BLUE_STAINED_GLASS)))
    val PURPLE_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.PURPLE_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.PURPLE_STAINED_GLASS)))
    val CYAN_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.CYAN_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.CYAN_STAINED_GLASS)))
    val LIGHT_GRAY_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.LIGHT_GRAY_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.LIGHT_GRAY_STAINED_GLASS)))
    val GRAY_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.GRAY_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.GRAY_STAINED_GLASS)))
    val PINK_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.PINK_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.PINK_STAINED_GLASS)))
    val LIME_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.LIME_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.LIME_STAINED_GLASS)))
    val YELLOW_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.YELLOW_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.YELLOW_STAINED_GLASS)))
    val LIGHT_BLUE_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.LIGHT_BLUE_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.LIGHT_BLUE_STAINED_GLASS)))
    val MAGENTA_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.MAGENTA_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.MAGENTA_STAINED_GLASS)))
    val ORANGE_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.ORANGE_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.ORANGE_STAINED_GLASS)))
    val WHITE_STAINED_GLASS_BARREL = register(GlassBarrelBlock(BlocksLib.WHITE_STAINED_GLASS_BARREL, Block.Settings.copy(Blocks.WHITE_STAINED_GLASS)))

    val BLACK_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BLACK_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BLACK_TERRACOTTA)))
    val RED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.RED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.RED_TERRACOTTA)))
    val GREEN_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.GREEN_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.GREEN_TERRACOTTA)))
    val BROWN_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BROWN_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BROWN_TERRACOTTA)))
    val BLUE_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BLUE_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BLUE_TERRACOTTA)))
    val PURPLE_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.PURPLE_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.PURPLE_TERRACOTTA)))
    val CYAN_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.CYAN_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.CYAN_TERRACOTTA)))
    val LIGHT_GRAY_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIGHT_GRAY_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIGHT_GRAY_TERRACOTTA)))
    val GRAY_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.GRAY_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.GRAY_TERRACOTTA)))
    val PINK_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.PINK_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.PINK_TERRACOTTA)))
    val LIME_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIME_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIME_TERRACOTTA)))
    val YELLOW_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.YELLOW_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.YELLOW_TERRACOTTA)))
    val LIGHT_BLUE_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIGHT_BLUE_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIGHT_BLUE_TERRACOTTA)))
    val MAGENTA_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.MAGENTA_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.MAGENTA_TERRACOTTA)))
    val ORANGE_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.ORANGE_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.ORANGE_TERRACOTTA)))
    val WHITE_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.WHITE_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.WHITE_TERRACOTTA)))

    val BLACK_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BLACK_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BLACK_GLAZED_TERRACOTTA)))
    val RED_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.RED_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.RED_GLAZED_TERRACOTTA)))
    val GREEN_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.GREEN_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.GREEN_GLAZED_TERRACOTTA)))
    val BROWN_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BROWN_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BROWN_GLAZED_TERRACOTTA)))
    val BLUE_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.BLUE_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.BLUE_GLAZED_TERRACOTTA)))
    val PURPLE_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.PURPLE_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.PURPLE_GLAZED_TERRACOTTA)))
    val CYAN_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.CYAN_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.CYAN_GLAZED_TERRACOTTA)))
    val LIGHT_GRAY_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIGHT_GRAY_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA)))
    val GRAY_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.GRAY_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.GRAY_GLAZED_TERRACOTTA)))
    val PINK_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.PINK_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.PINK_GLAZED_TERRACOTTA)))
    val LIME_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIME_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIME_GLAZED_TERRACOTTA)))
    val YELLOW_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.YELLOW_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.YELLOW_GLAZED_TERRACOTTA)))
    val LIGHT_BLUE_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.LIGHT_BLUE_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA)))
    val MAGENTA_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.MAGENTA_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.MAGENTA_GLAZED_TERRACOTTA)))
    val ORANGE_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.ORANGE_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.ORANGE_GLAZED_TERRACOTTA)))
    val WHITE_GLAZED_TERRACOTTA_BARREL = register(BarrelBlock(BlocksLib.WHITE_GLAZED_TERRACOTTA_BARREL, Block.Settings.copy(Blocks.WHITE_GLAZED_TERRACOTTA)))

    val BLACK_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.BLACK_CONCRETE_BARREL, Block.Settings.copy(Blocks.BLACK_CONCRETE)))
    val RED_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.RED_CONCRETE_BARREL, Block.Settings.copy(Blocks.RED_CONCRETE)))
    val GREEN_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.GREEN_CONCRETE_BARREL, Block.Settings.copy(Blocks.GREEN_CONCRETE)))
    val BROWN_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.BROWN_CONCRETE_BARREL, Block.Settings.copy(Blocks.BROWN_CONCRETE)))
    val BLUE_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.BLUE_CONCRETE_BARREL, Block.Settings.copy(Blocks.BLUE_CONCRETE)))
    val PURPLE_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.PURPLE_CONCRETE_BARREL, Block.Settings.copy(Blocks.PURPLE_CONCRETE)))
    val CYAN_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.CYAN_CONCRETE_BARREL, Block.Settings.copy(Blocks.CYAN_CONCRETE)))
    val LIGHT_GRAY_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.LIGHT_GRAY_CONCRETE_BARREL, Block.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE)))
    val GRAY_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.GRAY_CONCRETE_BARREL, Block.Settings.copy(Blocks.GRAY_CONCRETE)))
    val PINK_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.PINK_CONCRETE_BARREL, Block.Settings.copy(Blocks.PINK_CONCRETE)))
    val LIME_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.LIME_CONCRETE_BARREL, Block.Settings.copy(Blocks.LIME_CONCRETE)))
    val YELLOW_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.YELLOW_CONCRETE_BARREL, Block.Settings.copy(Blocks.YELLOW_CONCRETE)))
    val LIGHT_BLUE_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.LIGHT_BLUE_CONCRETE_BARREL, Block.Settings.copy(Blocks.LIGHT_BLUE_CONCRETE)))
    val MAGENTA_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.MAGENTA_CONCRETE_BARREL, Block.Settings.copy(Blocks.MAGENTA_CONCRETE)))
    val ORANGE_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.ORANGE_CONCRETE_BARREL, Block.Settings.copy(Blocks.ORANGE_CONCRETE)))
    val WHITE_CONCRETE_BARREL = register(BarrelBlock(BlocksLib.WHITE_CONCRETE_BARREL, Block.Settings.copy(Blocks.WHITE_CONCRETE)))

    val OAK_SIEVE = register(SieveBlock(BlocksLib.OAK_SIEVE, Block.Settings.copy(Blocks.OAK_WOOD)))
    val SPRUCE_SIEVE = register(SieveBlock(BlocksLib.SPRUCE_SIEVE, Block.Settings.copy(Blocks.SPRUCE_WOOD)))
    val BIRCH_SIEVE = register(SieveBlock(BlocksLib.BIRCH_SIEVE, Block.Settings.copy(Blocks.BIRCH_WOOD)))
    val JUNGLE_SIEVE = register(SieveBlock(BlocksLib.JUNGLE_SIEVE, Block.Settings.copy(Blocks.JUNGLE_WOOD)))
    val ACACIA_SIEVE = register(SieveBlock(BlocksLib.ACACIA_SIEVE, Block.Settings.copy(Blocks.ACACIA_WOOD)))
    val DARK_OAK_SIEVE = register(SieveBlock(BlocksLib.DARK_OAK_SIEVE, Block.Settings.copy(Blocks.DARK_OAK_WOOD)))

    val CRUCIBLE = register(CrucibleBlock())
    val RAW_CRUCIBLE = register(RawCrucibleBlock())

    fun registerBlocks() {
        register(CrucibleBlock())
        register(RawCrucibleBlock())

        register(KindlingBlock())

        for (block in modBlocks)
            event.registry.register(block)
    }

    private fun register(block: Block): Block {
        modBlocks.add(block)
        return block
    }
}
