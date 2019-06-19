package com.jozufozu.exnihiloomnia

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.proxy.CommonProxy
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager

import java.io.File

@Mod(ExNihilo.MODID)
class ExNihilo {
    init {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.addGenericListener(Block::class.java, ExNihiloBlocks::registerBlocks)
        MinecraftForge.EVENT_BUS.addGenericListener(Item::class.java, ExNihiloItems::registerItems)
    }

    private fun serverStarting(event: FMLServerStartingEvent) {
        event.server.resourceManager.addReloadListener(RegistryManager)
    }

    companion object {
        const val MODID = "exnihiloomnia"
        const val NAME = "Ex Nihilo Omnia"
        const val VERSION = "1.0"

        val log = LogManager.getLogger(NAME)

        var proxy: CommonProxy? = null

        var PATH: File? = null
    }
}
