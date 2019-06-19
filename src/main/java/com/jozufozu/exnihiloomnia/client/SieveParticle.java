package com.jozufozu.exnihiloomnia.client;

import net.minecraft.block.Block;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SieveParticle extends DiggingParticle
{
    public SieveParticle(World world, ItemStack mimic, BlockPos sievePos)
    {
        super(world, sievePos.getX() + world.rand.nextDouble() * 0.875 + 0.0625, sievePos.getY() + 8.5 / 16.0, sievePos.getZ() + world.rand.nextDouble() * 0.875 + 0.0625, 0, 0, 0, Block.getBlockFromItem(mimic.getItem()).getStateFromMeta(mimic.getMetadata()));
        
        this.setBlockPos(sievePos);
        
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        
        this.particleScale /= 5.0;
        this.maxAge *= 1.3;
    }
    
    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    
        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }
    
        this.motionY -= 0.002D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        
        this.motionY *= 0.9800000190734863D;
    
        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
