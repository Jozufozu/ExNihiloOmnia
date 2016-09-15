package exnihiloomnia.world.generation;

import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderVoidEnd extends WorldProviderEnd {

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderVoidEnd(worldObj, worldObj.getSeed());
    }
}
