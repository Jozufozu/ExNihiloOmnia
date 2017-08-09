package com.jozufozu.exnihiloomnia.common.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldTypeSkyblock extends WorldType
{
    public WorldTypeSkyblock()
    {
        super("exnihiloomnia_void");
    }
    
    @SubscribeEvent
    public void onPopulateChunk(PopulateChunkEvent event)
    {
    }
    
    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkGeneratorFlat(world, world.getSeed(), false, "3;minecraft:air;");
    }
}
