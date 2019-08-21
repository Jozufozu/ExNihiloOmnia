package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

class CrucibleBlock : RawCrucibleBlock(BlocksLib.CRUCIBLE, Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(3.0f)) {

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): Boolean {
        (worldIn.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            val held = player.getHeldItem(hand)

            var insert = ItemHandlerHelper.copyStackWithSize(held, 1)

            if (!FluidUtil.interactWithFluidHandler(player, hand, worldIn, pos, hit.face)) {
                it.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, hit.face).ifPresent { handler ->
                    insert = ItemHandlerHelper.insertItem(handler, insert, false)
                }
            } else {
                FluidUtil.getFluidContained(player.getHeldItem(hand)).ifPresent { fluid ->
                    worldIn.playSound(null, pos, fluid.fluid.getFillSound(fluid), SoundCategory.NEUTRAL, 1.0f, 1.0f)
                }
            }

            if (insert.isEmpty) {
                val blockFromItem = getBlockFromItem(held.item).defaultState

                if (blockFromItem !== Blocks.AIR.defaultState) {
                    val soundType = blockFromItem.getSoundType(worldIn, pos, player)
                    worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f)
                }
                held.shrink(1)
            }
        }

        return true
    }

    override fun hasTileEntity(state: BlockState) = true

    override fun createTileEntity(state: BlockState, world: IBlockReader) = CrucibleTileEntity()
}
