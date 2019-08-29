package com.jozufozu.exnihiloomnia.common.world

import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.IChunkGenerator
import java.util.*

class ChunkGeneratorSkyBlock(var world: World, seed: Long) : IChunkGenerator {
    var rand: Random = Random(seed)

    private var toGenerate: Biome? = null

    init {

        val name = ResourceLocation(ModConfig.world.biome)
        if (Biome.REGISTRY.containsKey(name)) {
            this.toGenerate = Biome.REGISTRY.getObject(name)
        }
    }

    override fun generateChunk(x: Int, z: Int): Chunk {
        val chunk = Chunk(this.world, x, z)

        if (this.toGenerate != null) {
            val biomeByte = Biome.getIdForBiome(this.toGenerate!!).toByte()

            val biome = ByteArray(256)

            for (i in 0..255) {
                biome[i] = biomeByte
            }

            chunk.biomeArray = biome
        }

        return chunk
    }


    override fun populate(x: Int, z: Int) {
        var x = x
        var z = z
        val spawnPoint = world.spawnPoint

        if (spawnPoint.x shr 4 == x && spawnPoint.z shr 4 == z) {
            loadSpawnIsland()

            if (spawnIsland != null) {
                world.worldInfo.setSpawn(spawnPoint)
                spawnIsland!!.placeInWorld(this.world, spawnPoint)
            } else {
                val gen = BlockPos.MutableBlockPos()

                x *= 16
                z *= 16

                val genState = Blocks.GRASS.defaultState

                for (xOff in 0..15) {
                    for (zOff in 0..15) {
                        gen.setPos(x + xOff, 64, z + zOff)

                        this.world.setBlockState(gen, genState)
                    }
                }
            }
        }
    }

    override fun generateStructures(chunkIn: Chunk, x: Int, z: Int): Boolean {
        return false
    }

    override fun getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): List<Biome.SpawnListEntry> {
        return emptyList()
    }

    override fun getNearestStructurePos(worldIn: World, structureName: String, position: BlockPos, findUnexplored: Boolean): BlockPos? {
        return null
    }

    override fun recreateStructures(chunkIn: Chunk, x: Int, z: Int) {

    }

    override fun isInsideStructure(worldIn: World, structureName: String, pos: BlockPos): Boolean {
        return false
    }

    companion object {
        var spawnIsland: SpawnIsland? = null

        fun loadSpawnIsland() {
            RegistryLoader.loadSingleJson("/spawn_island/" + ModConfig.world.spawnIsland + ".json") { `object` -> spawnIsland = SpawnIsland.deserialize(`object`) }
        }
    }
}
