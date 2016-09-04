package exnihiloomnia.world.generation;

import exnihiloomnia.world.ENOWorld;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderVoidHell extends WorldProviderHell{

	@Override
    public IChunkGenerator createChunkGenerator()
    {
		ChunkProviderVoidHell provider = new ChunkProviderVoidHell(this.worldObj, ENOWorld.getNetherFortressesAllowed(), worldObj.getSeed());
		
        return provider;
    }
}
