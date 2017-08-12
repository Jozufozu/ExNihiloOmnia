package com.jozufozu.exnihiloomnia.common.entity;

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityThrownStone extends EntityThrowable
{
    public EntityThrownStone(World worldIn)
    {
        super(worldIn);
    }
    
    public EntityThrownStone(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            int i = 4;
    
            if (result.entityHit instanceof EntityIronGolem)
            {
                i = 0;
            }
    
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) i);
        }
    
        if (!this.world.isRemote)
        {
            if (result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockPos = result.getBlockPos();
                IBlockState blockState = this.world.getBlockState(blockPos);
    
                //We want it to break glass, but some stuff (barrels) shouldn't
    
                Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
                
                if (motion.lengthSquared() >= 1.2 && blockState.getMaterial() == Material.GLASS && blockState.getBlockHardness(world, blockPos) <= 0.6f && !(blockState.getBlock() instanceof ITileEntityProvider))
                {
                    this.world.destroyBlock(blockPos, false);
                    this.motionX *= 0.8f;
                    this.motionZ *= 0.8f;
                    
                    return;
                }
                
                SoundType soundType = blockState.getBlock().getSoundType(blockState, this.world, blockPos, this);
                SoundEvent hitSound = soundType.getHitSound();
                this.world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, hitSound, SoundCategory.PLAYERS, 0.6f, soundType.getPitch() + this.world.rand.nextFloat() * 0.4f);
            }
            
            if (world.rand.nextFloat() <= 0.5f && !(this.getThrower() instanceof EntityPlayer && ((EntityPlayer) this.getThrower()).isCreative()))
                this.dropItem(ExNihiloItems.STONE, 1);
            
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }
}
