package com.jozufozu.exnihiloomnia.proxy

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.common.ExNihiloFluids
import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible
import com.jozufozu.exnihiloomnia.common.blocks.leaves.TileEntitySilkwormInfested
import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.items.ExNihiloMaterials
import com.jozufozu.exnihiloomnia.common.lib.LibMisc
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.ores.OreManager
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

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ConfigManager.sync(ExNihilo.MODID, Config.Type.INSTANCE)

        ExNihiloFluids.preInit()
        ExNihiloMaterials.preInit()

        ExNihiloTriggers.preInit()

        TileEntity.register("exnihiloomnia:sieve", TileEntitySieve::class.java)
        TileEntity.register("exnihiloomnia:crucible", TileEntityCrucible::class.java)
        TileEntity.register("exnihiloomnia:barrel", BarrelTileEntity::class.java)
        TileEntity.register("exnihiloomnia:infested_leaves", TileEntitySilkwormInfested::class.java)

        EntityRegistry.registerModEntity(LibMisc.ENTITY_STONE, EntityThrownStone::class.java, "thrown_stone", 0, ExNihilo, 64, 3, true)

        WorldTypeSkyblock.id // force the object to initialize
        WorldProviderSkyblock.preInit()
    }

    open fun init(event: FMLInitializationEvent) {
        ExNihiloFluids.init()
        ExNihiloNetwork.init()
        ExNihiloMaterials.init()
        RegistryLoader.loadRecipes()
        OreManager.initOreDict()
    }

    open fun postInit(event: FMLPostInitializationEvent) {

    }
}
