package exnihiloomnia.world.generation;

import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderEnd;

public class ChunkProviderVoidEnd extends ChunkProviderEnd {
	private World world;

	public ChunkProviderVoidEnd(World world, long par2) {
		super(world, false, par2);
		
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
    	if (x == 0 && z == 0) {
            //make the portal spawn ABOVE the void
			this.world.setBlockState(new BlockPos(0, this.world.rand.nextInt(32) + 40, 0), END_STONE);

			Template template = ENOWorld.getEndTemplate();
        	
        	if (template!= null) {
        		template.generate(world, 8, 8); //Begin generation in the center of chunk 0,0.
        	}
        }
    	else {
    		if (ENOWorld.getEndCrystalsAllowed()) {
				BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
				this.world.getBiome(blockpos.add(16, 0, 16)).decorate(this.world, this.world.rand, blockpos);
            }
    	}
    }
}
