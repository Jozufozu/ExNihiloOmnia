package exnihiloomnia.world.generation;

import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderVoidSurface extends WorldProviderSurface{

	@Override
	public DimensionType getDimensionType()
	{
		return DimensionType.OVERWORLD;
	}

	@Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkProviderVoidSurface(worldObj);
    }

    @Override
	public void createBiomeProvider()
	{
		if (!ENOWorld.getSurfaceBiome().equals("all"))
			this.biomeProvider = new BiomeProviderSingle(Biome.REGISTRY.getObject(new ResourceLocation(ENOWorld.getSurfaceBiome())));
		else
			super.createBiomeProvider();
	}
	
	@Override
    public boolean canCoordinateBeSpawn(int x, int z)
    {
		return true;
    }
	
	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		BlockPos spawn = new BlockPos(worldObj.getSpawnPoint());
		Template map = ENOWorld.getOverworldTemplate();
		
		if (map != null)
		{
			spawn = new BlockPos(spawn.getX(), map.getSpawnYLevel(), spawn.getZ());
		}
		else
		{
			spawn = worldObj.getTopSolidOrLiquidBlock(spawn);
		}
		
		return spawn;
	}
}
