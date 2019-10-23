package com.jozufozu.exnihiloomnia.common.world

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.math.BlockPos
import net.minecraft.world.DimensionType
import net.minecraft.world.WorldProviderSurface
import net.minecraftforge.common.DimensionManager

class WorldProviderSkyblock : WorldProviderSurface() {

    override fun getRandomizedSpawnPoint(): BlockPos {
        return if (world.worldInfo.terrainType === WorldTypeSkyblock) {
            world.spawnPoint
        } else super.getRandomizedSpawnPoint()
    }

    companion object {
        fun preInit() {
            try {
                DimensionManager.unregisterDimension(0)
                DimensionManager.registerDimension(0, DimensionType.register("Void Overworld", "overworld", 0, WorldProviderSkyblock::class.java, true))
            } catch (e: Exception) {
                ExNihilo.log.error("Failed to hijack world provider for the Overworld.")
            }

        }
    }
}
