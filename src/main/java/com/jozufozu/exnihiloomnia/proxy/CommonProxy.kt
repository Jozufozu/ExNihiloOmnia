package com.jozufozu.exnihiloomnia.proxy

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.crucible.CrucibleTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.sieve.SieveTileEntity
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.lib.MiscLib
import com.jozufozu.exnihiloomnia.common.world.WorldProviderSkyblock
import com.jozufozu.exnihiloomnia.common.world.WorldTypeSkyblock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import java.io.File

open class CommonProxy {
    fun preInit(event: FMLCommonSetupEvent) {
        ExNihilo.PATH = File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "exnihiloomnia" + File.separator)

        ConfigManager.sync(ExNihilo.MODID, Config.Type.INSTANCE)

        ExNihiloMaterials.preInit()

        EntityRegistry.registerModEntity(MiscLib.ENTITY_STONE, EntityThrownStone::class.java, "thrown_stone", 0, ExNihilo.INSTANCE, 64, 3, true)

        WorldTypeSkyblock.SKY_BLOCK = WorldTypeSkyblock()
        WorldProviderSkyblock.preInit()
    }

    fun init(event: FMLInitializationEvent) {
        ExNihiloMaterials.init()

        for (registry in ReloadableRegistry.getRegistries()) {
            registry.load()
        }
    }

    fun postInit(event: FMLPostInitializationEvent) {

    }
}
