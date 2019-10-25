package com.jozufozu.exnihiloomnia.client

import net.minecraft.block.Block
import net.minecraft.client.particle.DiggingParticle
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.ceil

class SieveParticle(world: World, mimic: ItemStack, sievePos: BlockPos)
    : DiggingParticle(
        world,
        sievePos.x.toDouble() + world.rand.nextDouble() * 0.875 + 0.0625,
        sievePos.y + 8.5 / 16.0,
        sievePos.z.toDouble() + world.rand.nextDouble() * 0.875 + 0.0625,
        0.0, 0.0, 0.0,
        Block.getBlockFromItem(mimic.item).defaultState) {
    init {
        setBlockPos(sievePos)

        motionX = 0.0
        motionY = 0.0
        motionZ = 0.0

        particleScale /= 5.0f
        maxAge = ceil(maxAge.toDouble() * 1.3).toInt()
    }

    override fun tick() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ

        if (age++ >= maxAge) {
            setExpired()
        }

        motionY -= 0.002 * particleGravity.toDouble()
        move(motionX, motionY, motionZ)

        motionY *= 0.9800000190734863

        if (onGround) {
            motionX *= 0.699999988079071
            motionZ *= 0.699999988079071
        }
    }
}
