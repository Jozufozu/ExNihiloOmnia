package exnihiloomnia.world.generation;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_BRIDGE;

import exnihiloomnia.util.helpers.PositionHelper;
import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkProviderVoidHell extends ChunkProviderHell {
	private World world;
	MapGenNetherBridge fortresses;
	
	public ChunkProviderVoidHell(World world, boolean shouldGenNetherFortress, long seed) {
		super(world, true, seed);
		this.world = world;
		
		this.fortresses = (MapGenNetherBridge) TerrainGen.getModdedMapGen(new MapGenNetherBridge(), NETHER_BRIDGE);
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
    	
    	if (ENOWorld.getNetherFortressesAllowed() && fortresses != null) {	
    		fortresses.generateStructure(world, world.rand, new ChunkPos(x,z));
        }
    }
}
