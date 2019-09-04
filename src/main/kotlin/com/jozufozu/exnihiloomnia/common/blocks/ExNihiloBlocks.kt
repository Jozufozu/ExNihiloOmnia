package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.*
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucible
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucibleRaw
import com.jozufozu.exnihiloomnia.common.blocks.sieve.BlockSieve
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object ExNihiloBlocks {
    private var registeredBlocks = false

    var modBlocks = ArrayList<Block>()

    val SIEVE: Block = register(BlockSieve())

    val CRUCIBLE: Block = register(BlockCrucible())
    val RAW_CRUCIBLE: Block = register(BlockCrucibleRaw())

    val BARREL_WOOD: Block = register(BlockBarrelWood())
    val BARREL_STAINED_GLASS: Block = register(BlockBarrelStainedGlass())
    val BARREL_CONCRETE: Block = register(BlockBarrelColored(LibBlocks.CONCRETE_BARREL, Material.ROCK).setHardness(1.8f))
    val BARREL_TERRACOTTA: Block = register(BlockBarrelColored(LibBlocks.TERRACOTTA_BARREL, Material.ROCK).setHardness(1.8f))
    val BARREL_STONE: Block = register(BlockBarrel(LibBlocks.STONE_BARREL, Material.ROCK).setHardness(1.5f).setResistance(10.0f))
    val BARREL_GLASS: Block = register(BlockBarrelGlass())

    val DUST: Block = register(BlockBaseFalling(LibBlocks.DUST, Material.SAND, SoundType.SNOW).setHardness(0.4f))
    val END_GRAVEL: Block = register(BlockBaseFalling(LibBlocks.END_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f))
    val NETHER_GRAVEL: Block = register(BlockBaseFalling(LibBlocks.NETHER_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f))

    val WITCHWATER: Block = register(BlockFluidWitchWater())

    fun hasRegisteredBlocks(): Boolean {
        return registeredBlocks
    }

    @SubscribeEvent
    @JvmStatic fun registerBlocks(event: RegistryEvent.Register<Block>) {
        for (block in modBlocks)
            event.registry.register(block)

        registeredBlocks = true
    }

    private fun <T: Block> register(block: T): T = block.also { modBlocks.add(it) }
}
