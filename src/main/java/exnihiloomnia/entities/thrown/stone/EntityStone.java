package exnihiloomnia.entities.thrown.stone;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityStone extends EntitySnowball {
	
    public EntityStone(World worldIn) {
        super(worldIn);
    }

    public EntityStone(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityStone(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult position) {
        if (position.entityHit != null) {
            position.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5f);
        }

        for (int i = 0; i < 10; ++i) {
            this.worldObj.spawnParticle(EnumParticleTypes.TOWN_AURA, position.hitVec.xCoord, position.hitVec.yCoord, position.hitVec.zCoord, 0, 0, 0);
        }

        worldObj.playSound(null, this.getPosition(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.PLAYERS, 0.5F, 1.0F);

        if (!this.worldObj.isRemote) {
            if (worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && worldObj.rand.nextInt(20) == 0) {
                EntitySilverfish fishy = new EntitySilverfish(this.worldObj);
                fishy.setLocationAndAngles(posX, posY, posZ, this.rotationYaw, 0);
                this.worldObj.spawnEntityInWorld(fishy);
            }

            this.setDead();
        }
    }
}