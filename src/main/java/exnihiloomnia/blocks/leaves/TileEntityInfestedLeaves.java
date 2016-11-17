package exnihiloomnia.blocks.leaves;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class TileEntityInfestedLeaves extends TileEntity implements ITickable {
    public IBlockState state = Blocks.LEAVES.getDefaultState();
    public boolean dying = false;
    public boolean permanent = false;

    private static final int maxSpreadTicks = 100;
    private int spreadTimer = 0;

    private static final float progressPerTick = .0005f;
    private float progress = 0;
    private float lastProgress = 0;

    @Override
    public void update() {
        if (lastProgress != progress)
            lastProgress = progress;

        if (progress < 1.0f)
            progress += progressPerTick;
        
        if (progress > 1.0f)
            progress = 1.0f;

        if (!getWorld().isRemote && progress > 0.6f) {
            spreadTimer++;

            if (spreadTimer >= maxSpreadTicks) {
                spread();
                spreadTimer = getWorld().rand.nextInt(10);
            }
        }
        
        if ((int) (progress * 100) % 50 == 0 && lastProgress != progress)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

        markDirty();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
    	this.progress = progress;
    }

    @SideOnly(Side.CLIENT)
    private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();

    @SideOnly(Side.CLIENT)
    public Color getColorForTint(int tintIndex) {
        Color ret;

        if (lastProgress != progress || !colors.containsKey(tintIndex)) {
            float percentage = getProgress();

            int color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(this.state, getWorld(), this.pos, tintIndex);

            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;

            ret = new Color((r + (255 - r) * percentage) / 255f, (g + (255 - g) * percentage) / 255f, (b + (255 - b) * percentage) / 255f, 1f);

            colors.remove(tintIndex);
            colors.put(tintIndex, ret);
        }
        else
            ret = colors.get(tintIndex);

        return ret;
    }

    private void spread() {
        int x = this.getWorld().rand.nextInt(3) - 1;
        int y = this.getWorld().rand.nextInt(3) - 1;
        int z = this.getWorld().rand.nextInt(3) - 1;

        int placeX = pos.getX() + x;
        int placeY = pos.getY() + y;
        int placeZ = pos.getZ() + z;

        BlockPos place = new BlockPos(placeX, placeY, placeZ);

        if (infest(getWorld(), place, false)) {

            TileEntityInfestedLeaves te = (TileEntityInfestedLeaves) getWorld().getTileEntity(place);

            if (te != null)
                te.setProgress(((float) getWorld().rand.nextInt(15)) / 100);
        }
    }

    public static boolean infest(World world, BlockPos pos, boolean makeNoise) {
        IBlockState target = world.getBlockState(pos);
        Block block = target.getBlock();

        if (block.isLeaves(target, world, pos) && !block.equals(ENOBlocks.INFESTED_LEAVES) && !("forestry".equals(Block.REGISTRY.getNameForObject(block).getResourceDomain()) && block instanceof ITileEntityProvider)) {

            world.setBlockState(pos, ENOBlocks.INFESTED_LEAVES.getDefaultState(), 2);

            TileEntityInfestedLeaves te = (TileEntityInfestedLeaves) world.getTileEntity(pos);

            if (te != null)
                te.setMimicBlock(target);

            if (makeNoise)
                world.playSound(null, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 0.5f, 1.0f);

            return true;
        }

        return false;
    }

    public IBlockState getState() {
        return state;
    }

    public void setMimicBlock(IBlockState state) {
        this.state = state;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.progress = compound.getFloat("progress");

        if (!compound.getString("block").equals(""))
            this.state = Block.getBlockFromName(compound.getString("block")).getStateFromMeta(compound.getInteger("meta"));

        this.dying = compound.getBoolean("dying");
        this.permanent = compound.getBoolean("permanent");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("progress", this.progress);

        if (this.state == null) {
            compound.setString("block", "");
        }
        else {
            compound.setString("block", Block.REGISTRY.getNameForObject(this.state.getBlock()).toString());
        }

        compound.setInteger("meta", this.state.getBlock().getMetaFromState(this.state));

        compound.setBoolean("dying", dying);
        compound.setBoolean("permanent", permanent);

        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);

        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
    }
}
