package exnihiloomnia.world.generation;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_BRIDGE;

import exnihiloomnia.util.helpers.PositionHelper;
import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.List;

public class ChunkProviderVoidHell extends ChunkProviderHell {
	private World world;
	private MapGenNetherBridge fortresses = new MapGenNetherBridge();
	
	public ChunkProviderVoidHell(World world, boolean shouldGenNetherFortress, long seed) {
		super(world, shouldGenNetherFortress, seed);
		this.world = world;

		this.fortresses = (MapGenNetherBridge) TerrainGen.getModdedMapGen(fortresses, NETHER_BRIDGE);
	}

	@Override
	public Chunk provideChunk(int x, int z) {
		ChunkPrimer primer = new ChunkPrimer();
		Chunk chunk = new Chunk(world, primer, x, z);
        
        if (ENOWorld.getNetherFortressesAllowed() && fortresses != null) {
            this.fortresses.generate(world, x, z, primer);
        }
        
        chunk.generateSkylightMap();
        
        return chunk;
    }
    
    @Override
    public void populate(int x, int z) {
    	if (PositionHelper.isChunkSpawn(world, world.getChunkFromChunkCoords(x, z))) {
        	Template template = ENOWorld.getNetherTemplate();
        	
        	if (template!= null) {
        		double xSpawn = (double)world.getWorldInfo().getSpawnX() / world.provider.getMovementFactor();
        		double zSpawn = (double)world.getWorldInfo().getSpawnZ() / world.provider.getMovementFactor();
        		
        		template.generate(world, (int)xSpawn, (int)zSpawn);
        	}
        }
		fortresses.generateStructure(world, world.rand, new ChunkPos(x,z));
    }

    @Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		if (creatureType == EnumCreatureType.MONSTER)
		{
			if (this.fortresses.isInsideStructure(pos))
			{
				return this.fortresses.getSpawnList();
			}

			if (this.fortresses.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK)
			{
				return this.fortresses.getSpawnList();
			}
		}

		Biome biome = this.world.getBiomeGenForCoords(pos);
		return biome.getSpawnableList(creatureType);
	}

    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        this.fortresses.generate(this.world, x, z, null);
    }
}
