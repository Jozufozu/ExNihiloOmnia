package com.jozufozu.exnihiloomnia.common.world;

import com.jozufozu.exnihiloomnia.common.ModConfig;
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChunkGeneratorSkyBlock implements IChunkGenerator
{
    public static SpawnIsland spawnIsland;
    
    public World world;
    public Random rand;
    
    public Biome toGenerate;
    
    public ChunkGeneratorSkyBlock(World world, long seed)
    {
        this.world = world;
        this.rand = new Random(seed);
    
        ResourceLocation name = new ResourceLocation(ModConfig.world.biome);
        if (Biome.REGISTRY.containsKey(name))
        {
            this.toGenerate = Biome.REGISTRY.getObject(name);
        }
    }

    public static void loadSpawnIsland()
    {
        RegistryLoader.loadSingleJson("/spawn_island/" + ModConfig.world.spawnIsland + ".json", object -> spawnIsland = SpawnIsland.deserialize(object));
    }
    
    @Override
    public Chunk generateChunk(int x, int z)
    {
        Chunk chunk = new Chunk(this.world, x, z);
        
        if (this.toGenerate != null)
        {
            byte biomeByte = (byte) Biome.getIdForBiome(this.toGenerate);
    
            byte[] biome = new byte[256];
    
            for (int i = 0; i < 256; i++)
            {
                biome[i] = biomeByte;
            }
    
            chunk.setBiomeArray(biome);
        }
        
        return chunk;
    }
    
    
    
    @Override
    public void populate(int x, int z)
    {
        BlockPos spawnPoint = world.getSpawnPoint();
        
        if (spawnPoint.getX() >> 4 == x && spawnPoint.getZ() >> 4 == z)
        {
            loadSpawnIsland();
            
            if (spawnIsland != null)
            {
                world.getWorldInfo().setSpawn(spawnPoint);
                spawnIsland.placeInWorld(this.world, spawnPoint);
            }
            else
            {
                BlockPos.MutableBlockPos gen = new BlockPos.MutableBlockPos();
    
                x *= 16;
                z *= 16;
    
                IBlockState genState = Blocks.GRASS.getDefaultState();
    
                for (int xOff = 0; xOff < 16; xOff++)
                {
                    for (int zOff = 0; zOff < 16; zOff++)
                    {
                        gen.setPos(x + xOff, 64, z + zOff);
            
                        this.world.setBlockState(gen, genState);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return Collections.emptyList();
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }
    
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
    
    }
    
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}
