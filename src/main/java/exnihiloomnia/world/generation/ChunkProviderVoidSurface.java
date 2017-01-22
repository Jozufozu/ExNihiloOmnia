package exnihiloomnia.world.generation;

import exnihiloomnia.ENO;
import exnihiloomnia.util.helpers.PositionHelper;
import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderOverworld;

public class ChunkProviderVoidSurface extends ChunkProviderOverworld {
	private final World world;

    public ChunkProviderVoidSurface(World world) {
        super(world, world.getSeed(), false, "");
        
        this.world = world;
    }
    
    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = new Chunk(world, new ChunkPrimer(), x, z);

        chunk.generateSkylightMap();
        
        return chunk;
    }
    
    @Override
    public void populate(int x, int z) {
    	if (PositionHelper.isChunkSpawn(world, world.getChunkFromChunkCoords(x, z))) {
        	Template template = ENOWorld.getOverworldTemplate();
        	
        	if (template!= null) {
        		template.generate(world, world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnZ());
        	}
        	else {
        		ENO.log.error("Failed to load overworld map template.");
        	}
        }
    }
}
