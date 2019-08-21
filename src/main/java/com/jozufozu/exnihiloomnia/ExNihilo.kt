package com.jozufozu.exnihiloomnia

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloTileEntities
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.proxy.CommonProxy
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.io.File

object ExNihilo {
    const val MODID = "exnihiloomnia"
    const val NAME = "Ex Nihilo Omnia"
    const val VERSION = "1.0"

    val log: Logger = LogManager.getLogger(NAME)

    var PATH: File? = null

    override fun onInitialize() {
        FMLJavaModLoadingContext.get().modEventBus.addListener(this::serverStarting)
        MinecraftForge.EVENT_BUS.addGenericListener(Block::class.java, ExNihiloBlocks::registerBlocks)
        MinecraftForge.EVENT_BUS.addGenericListener(Item::class.java, ExNihiloItems::registerItems)
        MinecraftForge.EVENT_BUS.addGenericListener(TileEntityType::class.java, ExNihiloTileEntities::register)
    }

    private fun serverStarting(event: FMLServerStartingEvent) {
        event.server.resourceManager.addReloadListener(RegistryManager)
    }
}
