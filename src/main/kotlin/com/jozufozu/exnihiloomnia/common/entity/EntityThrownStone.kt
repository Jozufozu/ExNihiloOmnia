package com.jozufozu.exnihiloomnia.common.entity

import com.jozufozu.exnihiloomnia.common.ExNihiloTags
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ThrowableEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World

class EntityThrownStone : ThrowableEntity {
    constructor(worldIn: World) : super(worldIn) {}

    constructor(worldIn: World, throwerIn: LivingEntity) : super(worldIn, throwerIn) {}

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    override fun onImpact(result: RayTraceResult) {
        if (result.type == RayTraceResult.Type.ENTITY && result is EntityRayTraceResult) {
            var i = 4

            if (result.entity is IronGolemEntity) {
                i = 0
            }

            result.entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.thrower), i.toFloat())
        }

        if (!this.world.isRemote) {
            if (result.type == RayTraceResult.Type.BLOCK && result is BlockRayTraceResult) {
                val blockPos = result.pos
                val blockState = this.world.getBlockState(blockPos)

                //We want it to break glass, but some stuff (barrels) shouldn't

                if (motion.lengthSquared() >= 1.2 && ExNihiloTags.Blocks.THROWN_STONE_BREAKING.contains(blockState.block)) {
                    this.world.destroyBlock(blockPos, false)

                    setMotion(motion.x * 0.8, motion.y, motion.z * 0.8)

                    return
                }

                val soundType = blockState.block.getSoundType(blockState, this.world, blockPos, this)
                val hitSound = soundType.hitSound
                this.world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, hitSound, SoundCategory.PLAYERS, 0.6f, soundType.getPitch() + this.world.rand.nextFloat() * 0.4f)
            }

            if (world.rand.nextFloat() <= 0.5f && !(this.thrower is PlayerEntity && (this.thrower as PlayerEntity).isCreative))
                this.entityDropItem(ExNihiloItems.STONE)

            this.world.setEntityState(this, 3)
            this.remove()
        }
    }
}