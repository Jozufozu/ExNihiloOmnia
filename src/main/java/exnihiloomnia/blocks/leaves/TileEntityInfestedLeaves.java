package exnihiloomnia.blocks.leaves;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityInfestedLeaves extends TileEntity implements ITickable {
    public IBlockState state = Blocks.LEAVES.getDefaultState();
    public boolean dying = false;
    public boolean permanent = false;

    private static final int maxSpreadTicks = 100;
    private int spreadTimer = 0;

    private static final float progressPerTick = .0005f;
    private float progress = 0;

    @Override
    public void update() {
        if (progress < 1.0f) {
            progress += progressPerTick;
        }
        
        if (progress > 1.0f) {
            progress = 1.0f;
        }

        if (!worldObj.isRemote && progress > 0.6f) {
            spreadTimer++;

            if (spreadTimer >= maxSpreadTicks) {
                spread();
                spreadTimer = worldObj.rand.nextInt(10);
            }
        }
        
        if ((int) (progress * 100) % 20 == 0 && progress != 1) {
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        }
        markDirty();
    }

    public boolean isComplete() {
        return progress >= progressPerTick;
    }

    public Color getRenderColor(IBlockState state) {
        Color base = new Color(Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, getWorld(), getPos(), 0));

        return Color.average(base, Color.WHITE, getProgress());
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
    	this.progress = progress;
    }

    private void spread() {
        int x = this.worldObj.rand.nextInt(3) - 1;
        int y = this.worldObj.rand.nextInt(3) - 1;
        int z = this.worldObj.rand.nextInt(3) - 1;

        int placeX = pos.getX() + x;
        int placeY = pos.getY() + y;
        int placeZ = pos.getZ() + z;

        BlockPos place = new BlockPos(placeX, placeY, placeZ);

        IBlockState target = worldObj.getBlockState(place);

        if (target.getBlock().isLeaves(target, worldObj, place) && !target.getBlock().equals(ENOBlocks.INFESTED_LEAVES)) {
            IBlockState block = worldObj.getBlockState(place);

            worldObj.setBlockState(place, ENOBlocks.INFESTED_LEAVES.getDefaultState(), 2);
            TileEntityInfestedLeaves te = (TileEntityInfestedLeaves)worldObj.getTileEntity(place);

            if (te != null) {
                te.setMimicBlock(block);
                te.setProgress(((float) worldObj.rand.nextInt(15))/100);
            }
        }
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

        if (!compound.getString("state").equals(""))
            this.state = Block.getBlockFromName(compound.getString("state")).getStateFromMeta(compound.getInteger("meta"));

        this.dying = compound.getBoolean("dying");
        this.permanent = compound.getBoolean("permanent");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("progress", this.progress);
        
        if (this.state == null) {
            compound.setString("state", "");
        }
        else {
            compound.setString("state", Block.REGISTRY.getNameForObject(this.state.getBlock()).toString());
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
