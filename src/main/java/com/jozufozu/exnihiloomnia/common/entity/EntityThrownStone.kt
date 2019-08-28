package com.jozufozu.exnihiloomnia.common.entity

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class EntityThrownStone : EntityThrowable {
    constructor(worldIn: World) : super(worldIn) {}

    constructor(worldIn: World, throwerIn: EntityLivingBase) : super(worldIn, throwerIn) {}

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    override fun onImpact(result: RayTraceResult) {
        if (result.entityHit != null) {
            var i = 4

            if (result.entityHit is EntityIronGolem) {
                i = 0
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), i.toFloat())
        }

        if (!this.world.isRemote) {
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                val blockPos = result.blockPos
                val blockState = this.world.getBlockState(blockPos)

                //We want it to break glass, but some stuff (barrels) shouldn't

                val motion = Vec3d(this.motionX, this.motionY, this.motionZ)

                if (motion.lengthSquared() >= 1.2 && blockState.material === Material.GLASS && blockState.getBlockHardness(world, blockPos) <= 0.6f && blockState.block !is ITileEntityProvider) {
                    this.world.destroyBlock(blockPos, false)
                    this.motionX *= 0.8
                    this.motionZ *= 0.8

                    return
                }

                val soundType = blockState.block.getSoundType(blockState, this.world, blockPos, this)
                val hitSound = soundType.hitSound
                this.world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, hitSound, SoundCategory.PLAYERS, 0.6f, soundType.getPitch() + this.world.rand.nextFloat() * 0.4f)
            }

            if (world.rand.nextFloat() <= 0.5f && !(this.getThrower() is EntityPlayer && (this.getThrower() as EntityPlayer).isCreative))
                this.dropItem(ExNihiloItems.STONE, 1)

            this.world.setEntityState(this, 3.toByte())
            this.setDead()
        }
    }
}
