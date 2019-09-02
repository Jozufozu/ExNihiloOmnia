package com.jozufozu.exnihiloomnia.proxy

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible
import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.items.ExNihiloMaterials
import com.jozufozu.exnihiloomnia.common.lib.LibMisc
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.world.WorldProviderSkyblock
import com.jozufozu.exnihiloomnia.common.world.WorldTypeSkyblock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.EntityRegistry
import java.io.File

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ExNihilo.PATH = File(event.modConfigurationDirectory.absolutePath + File.separator + "exnihiloomnia" + File.separator)

        ConfigManager.sync(ExNihilo.MODID, Config.Type.INSTANCE)

        ExNihiloMaterials.preInit()
        ExNihiloTriggers.preInit()

        RegistryLoader.loadOres()

        TileEntity.register("exnihiloomnia:sieve", TileEntitySieve::class.java)
        TileEntity.register("exnihiloomnia:crucible", TileEntityCrucible::class.java)
        TileEntity.register("exnihiloomnia:barrel", TileEntityBarrel::class.java)

        EntityRegistry.registerModEntity(LibMisc.ENTITY_STONE, EntityThrownStone::class.java, "thrown_stone", 0, ExNihilo, 64, 3, true)

        WorldTypeSkyblock.SKY_BLOCK = WorldTypeSkyblock()
        WorldProviderSkyblock.preInit()
    }

    fun init(event: FMLInitializationEvent) {
        ExNihiloNetwork.init()
        ExNihiloMaterials.init()
        RegistryLoader.loadRecipes()
    }

    fun postInit(event: FMLPostInitializationEvent) {

    }
}
