package exnihiloomnia.blocks.leaves;

import java.util.ArrayList;
import java.util.List;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityInfestedLeaves extends TileEntity implements ITickable {
    public Block block = Blocks.LEAVES;
    public int meta = 0;
    public Color color = Color.WHITE;
    public boolean dying = false;
    public boolean permanent = false;

    private static final int SPREAD_INTERVAL = 100;
    private int spreadTimer = 0;

    private static final float PROGRESS_INTERVAL = .0005f;
    private float progress = 0;

    @Override
    public void update() {
        if (progress < 1.0f) {
            progress += PROGRESS_INTERVAL;
        }
        
        if (progress > 1.0f) {
            progress = 1.0f;
        }

        if (!worldObj.isRemote && progress > 0.6f) {
            spreadTimer++;

            if (spreadTimer >= SPREAD_INTERVAL) {
                spread();
                spreadTimer = worldObj.rand.nextInt(10);
            }
        }
        
        if ((int) (progress * 100) % 20 == 0 && progress != 1)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        
        markDirty();
    }

    public boolean isComplete() {
        return progress >= PROGRESS_INTERVAL;
    }

    public Color getRenderColor() {
        Color base = new Color(worldObj.getBiomeForCoordsBody(pos).getFoliageColorAtPos(pos));
        Color white = Color.WHITE;

        return Color.average(base, white, getProgress());
    }

    public int getBrightness() {
        return block.getLightValue(worldObj.getBlockState(pos), worldObj, pos);
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
            Block block = worldObj.getBlockState(place).getBlock();
            int meta = worldObj.getBlockState(place).getBlock().getMetaFromState(worldObj.getBlockState(place));
            worldObj.setBlockState(place, ENOBlocks.INFESTED_LEAVES.getDefaultState(), 2);
            TileEntityInfestedLeaves te = (TileEntityInfestedLeaves)worldObj.getTileEntity(place);

            if (te != null) {
                te.setMimicBlock(block, meta);
                te.setProgress(((float) worldObj.rand.nextInt(15))/100);
            }
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public void setMimicBlock(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.progress = compound.getFloat("progress");

        if (!compound.getString("block").equals(""))
            this.block = Block.getBlockFromName(compound.getString("block"));

        this.meta = compound.getInteger("meta");
        this.dying = compound.getBoolean("dying");
        this.permanent = compound.getBoolean("permanent");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("progress", this.progress);
        
        if (this.block == null) {
            compound.setString("block", "");
        }
        else {
            compound.setString("block", Block.REGISTRY.getNameForObject(this.block).toString());
        }
        
        compound.setInteger("meta", this.meta);

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
