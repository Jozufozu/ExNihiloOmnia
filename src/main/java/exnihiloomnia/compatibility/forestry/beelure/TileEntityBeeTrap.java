package exnihiloomnia.compatibility.forestry.beelure;

import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.compatibility.forestry.Hive;
import exnihiloomnia.compatibility.forestry.HiveRegistry;
import exnihiloomnia.compatibility.forestry.Surroundings;
import exnihiloomnia.util.helpers.PositionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

public class TileEntityBeeTrap extends TileEntity implements ITickable {
    public static PositionHelper helper = new PositionHelper();

    private Surroundings blocks = new Surroundings();
    public static final int TIMER_MAX = 6000;
    private int timer = 0;

    private static final int MAX_X = 2;
    private static final int MIN_X = -2;
    private int x = MIN_X;

    private static final int MAX_Y = 2;
    private static final int MIN_Y = -2;
    private int y = MIN_Y;

    private static final int MAX_Z = 2;
    private static final int MIN_Z = -2;
    private int z = MIN_Z;

    //Spawn chance, higher = more rare.
    private static final int HIVE_SPAWN_CHANCE = 50;

    private boolean complete = false;

    @Override
    public void update() {
        if (!getWorld().isRemote && Loader.isModLoaded("forestry")) {
            timer++;

            //Scan one block per tick. Nice and slow. No lag.
            if (x > MAX_X) {
                x = MIN_X;
                y++;
            }

            if (y > MAX_Y) {
                y = MIN_Y;
                z++;
            }

            if (z > MAX_Z) {
                z = MIN_Z;
                complete = true;
            }

            if (complete) {
                boolean canSeeSky = helper.isTopBlock(getWorld(), pos);
                Biome biome = getWorld().getBiome(pos);

                //check to see if the current location meets the requirements for a hive to spawn.
                Hive hive = HiveRegistry.getHive(biome, blocks, canSeeSky);

                //If hive != null, replace this block with the returned hive.
                if (hive != null && getWorld().rand.nextInt(HIVE_SPAWN_CHANCE - Math.min(30, hive.getSpawnChanceModifier(blocks))) == 0)
                    getWorld().setBlockState(pos, hive.state, 3);

                else {
                    //Reset the scanner if spawning failed.
                    blocks = new Surroundings();
                    complete = false;
                }
            } else {
                BlockPos pos = this.pos.add(x, y, z);
                //scan not complete, continue scanning.
                IBlockState block = getWorld().getBlockState(pos);

                blocks.addBlock(getWorld(), pos);
                if (x == 0 && y == 1 && z == 0)
                    blocks.setBlockAbove(block);

                x++;
            }

            //If the timer expires, then we require re-treatment with seed oil.
            if (timer > TIMER_MAX)
                getWorld().setBlockState(pos, ENOCompatibility.BEE_TRAP.getDefaultState(), 3);
        }
    }

    public int getTimer() {
        return timer;
    }

    public Surroundings getBlocks() {
        return blocks;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        timer = compound.getInteger("timer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("timer", timer);

        return compound;
    }
}
