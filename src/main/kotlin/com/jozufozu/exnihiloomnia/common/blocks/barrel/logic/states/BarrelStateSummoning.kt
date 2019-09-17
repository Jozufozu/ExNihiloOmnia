package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.CountdownLogic
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.registries.recipes.SummoningRecipe
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.world.chunk.storage.AnvilChunkLoader

class BarrelStateSummoning(val recipe: SummoningRecipe, name: ResourceLocation) : BarrelState(name) {
    init {
        logic.add(CountdownLogic({ recipe.time }, {
            val pos = it.pos
            AnvilChunkLoader.readWorldEntityPos(recipe.entityNBT, it.world, pos.x + 0.5, pos.y + 1.0, pos.z + 0.5, true)

            recipe.summonSound?.let { sound ->
                it.world.playSound(null, it.pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f)
            }

            it.state = BarrelStates.EMPTY
        }))
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        val progress: Float = (recipe.time - barrel.timer).toFloat() / recipe.time
        val tint = Color.weightedAverage(Color.WHITE, recipe.color, progress)

        val fluid = barrel.fluid?.fluid ?: return
        drawFluid(
                fluid,
                barrel.fluidAmount.toFloat() / TileEntityBarrel.fluidCapacity.toFloat(),
                barrel.fluidAmountLastTick.toFloat() / TileEntityBarrel.fluidCapacity.toFloat(),
                x, y, z, partialTicks,
                tint,
                barrel.world.getBlockState(barrel.pos).material.isOpaque)
    }
}