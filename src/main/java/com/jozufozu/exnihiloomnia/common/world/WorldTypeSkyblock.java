package com.jozufozu.exnihiloomnia.common.world;

import com.jozufozu.exnihiloomnia.common.ModConfig;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeSkyblock extends WorldType
{
    public static WorldType SKY_BLOCK;
    
    public WorldTypeSkyblock()
    {
        super("exnihiloomnia");
    }
    
    @Override
    public float getCloudHeight()
    {
        return ModConfig.world.cloudLevel;
    }
    
    @Override
    public int getMinimumSpawnHeight(World world)
    {
        return ModConfig.world.spawnLevel;
    }
    
    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkGeneratorSkyBlock(world, world.getSeed());
    }
}
