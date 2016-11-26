package exnihiloomnia.compatibility.forestry;


import forestry.api.apiculture.FlowerManager;
import forestry.core.genetics.alleles.EnumAllele.Flowers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class Surroundings {
    public Map<IBlockState, Integer> blocks = new HashMap<IBlockState, Integer>();
    public Map<Flowers, Integer> flowers = new HashMap<Flowers, Integer>();
    public int leafCount;

    public String blockAbove;

    public void addBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock().isLeaves(state, world, pos))
            leafCount++;

        if (blocks.containsKey(state)) {
            int count = blocks.get(state);

            blocks.put(state, count + 1);
        }
        else
            blocks.put(state, 1);

        tryAddFlower(world, pos);
    }

    public void setBlockAbove(IBlockState state) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);

        this.blockAbove = block + ":" + meta;
    }

    public void tryAddFlower(World world, BlockPos pos) {

        for (Flowers flower : Flowers.values()) {
            if (FlowerManager.flowerRegistry.isAcceptedFlower(flower.getValue().getFlowerType(), world, pos))
                addFlower(flower);
        }
    }

    private void addFlower(Flowers key) {

        if (flowers.containsKey(key)) {
            int count = flowers.get(key);

            flowers.put(key, count + 1);
        }
        else
            flowers.put(key, 1);
    }

    public int getFlowerCount(Flowers type) {

        if (flowers.containsKey(type))
            return flowers.get(type);
        else
            return 0;
    }
}
