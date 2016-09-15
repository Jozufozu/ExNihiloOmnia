package exnihiloomnia.blocks.automation.tile;

import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileSifter extends TileEntity implements ITickable {
    private TileEntitySieve attachedSieve;

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            if (attachedSieve == null) {
                TileEntity sieve = worldObj.getTileEntity(getPos().up());
                
                if (sieve != null)
                    attachedSieve = (TileEntitySieve) worldObj.getTileEntity(getPos().up());
            }
            else { //automate
                if (attachedSieve.canWork() && worldObj.getWorldTime() % 5 == 0) {
                    worldObj.playSound(null, attachedSieve.getPos(), SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 0.3f, 0.6f);
                    attachedSieve.doWork();
                }
            }
        }
    }
}
