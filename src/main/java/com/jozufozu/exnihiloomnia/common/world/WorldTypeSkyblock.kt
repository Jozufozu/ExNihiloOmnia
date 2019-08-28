package com.jozufozu.exnihiloomnia.common.world

import com.jozufozu.exnihiloomnia.common.ModConfig
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.gen.IChunkGenerator

class WorldTypeSkyblock : WorldType("exnihiloomnia") {

    override fun getCloudHeight(): Float {
        return ModConfig.world.cloudLevel.toFloat()
    }

    override fun getMinimumSpawnHeight(world: World): Int {
        return ModConfig.world.spawnLevel
    }

    override fun getChunkGenerator(world: World, generatorOptions: String): IChunkGenerator {
        return ChunkGeneratorSkyBlock(world, world.seed)
    }

    companion object {
        var SKY_BLOCK: WorldType? = null
    }
}
