package com.jozufozu.exnihiloomnia.common.entity;

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityThrownStone extends ThrowableEntity
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
        if (result.getType() == RayTraceResult.Type.ENTITY)
        {
            int i = 4;
    
            if (result.entityHit instanceof IronGolemEntity)
            {
                i = 0;
            }
    
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) i);
        }
    
        if (!this.world.isRemote)
        {
            if (result.getType() == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockPos = result.getBlockPos();
                BlockState blockState = this.world.getBlockState(blockPos);
    
                //We want it to break glass, but some stuff (barrels) shouldn't

                if (getMotion().lengthSquared() >= 1.2 && blockState.getMaterial() == Material.GLASS && blockState.getBlockHardness(world, blockPos) <= 0.6f && !blockState.hasTileEntity())
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
            
            if (world.rand.nextFloat() <= 0.5f && !(this.getThrower() instanceof PlayerEntity && ((PlayerEntity) this.getThrower()).isCreative()))
                this.dropItem(ExNihiloItems.STONE, 1);
            
            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
}
