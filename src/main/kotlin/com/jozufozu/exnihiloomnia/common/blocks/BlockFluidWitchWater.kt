package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.ExNihiloFluids
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidClassic


class BlockFluidWitchWater : BlockFluidClassic(ExNihiloFluids.WITCHWATER, Material.WATER) {

    init {
        registryName = BlocksLib.WITCHWATER
    }

    override fun onEntityCollidedWithBlock(world: World, pos: BlockPos, state: IBlockState, entity: Entity) {
        if (!world.isRemote && !entity.isDead) {
            if (entity is EntityVillager) {
                if (world.difficulty !== EnumDifficulty.PEACEFUL) {
                    if (!entity.isChild) {
                        replaceEntity(world, entity, EntityWitch(world))
                    } else {
                        val zombie = replaceEntity(world, entity, EntityZombieVillager(world))
                        zombie.forgeProfession = entity.professionForge
                        zombie.isChild = entity.isChild
                    }
                }
            }

            if (entity is EntityGuardian && entity !is EntityElderGuardian) replaceEntity(world, entity, EntityElderGuardian(world))
            if (entity is EntityPig) replaceEntity(world, entity, EntityPigZombie(world))
            if (entity is EntityCow && entity !is EntityMooshroom) replaceEntity(world, entity, EntityMooshroom(world))
            if (entity is EntitySkeleton) replaceEntity(world, entity, EntityWitherSkeleton(world))
            if (entity is EntitySpider && entity !is EntityCaveSpider) replaceEntity(world, entity, EntityCaveSpider(world))
            if (entity is EntitySquid && world.difficulty !== EnumDifficulty.PEACEFUL) replaceEntity(world, entity, EntityGhast(world))

            if (entity is EntityCreeper) {
                if (!entity.powered) {
                    entity.onStruckByLightning(null)
                    entity.health = entity.maxHealth
                }
            }

            if (entity is EntityPlayer) {
                entity.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 210, 2))
                entity.addPotionEffect(PotionEffect(MobEffects.WITHER, 210, 0))
                entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 210, 0))
            }
        }
    }

    private fun <T: EntityLivingBase> replaceEntity(world: World, old: EntityLivingBase, new: T): T {
        old.setDead()

        new.setLocationAndAngles(old.posX, old.posY + 2, old.posZ, old.rotationYaw, old.rotationPitch)
        new.renderYawOffset = old.renderYawOffset
        new.health = new.maxHealth

        world.spawnEntity(new)

        return new
    }
}