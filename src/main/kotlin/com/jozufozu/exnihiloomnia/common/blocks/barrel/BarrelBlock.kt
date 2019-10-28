package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlock
import com.jozufozu.exnihiloomnia.common.blocks.ModBlock
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.IEnviromentBlockReader
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

open class BarrelBlock(registryName: ResourceLocation, properties: Properties) : ModBlock(registryName, properties) {
    init {
        INSTANCES.add(this)
    }

    companion object {
        @JvmField val INSTANCES: ArrayList<BarrelBlock> = ArrayList()
        @JvmField val BARREL_AABB: VoxelShape = VoxelShapes.create(1.0 / 16.0, 0.0, 1.0 / 16.0, 15.0 / 16.0, 1.0, 15.0 / 16.0)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, playerIn: PlayerEntity, hand: Hand, blockRayTraceResult: BlockRayTraceResult): Boolean {
        if (worldIn.isRemote) return true
        val barrel = worldIn.getTileEntity(pos) as? BarrelTileEntity ?: return true

        val held = playerIn.getHeldItem(hand)

        if (barrel.state.canExtractItems(barrel) && playerIn.isSneaking && held.isEmpty) {
            barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent {
                ItemHandlerHelper.giveItemToPlayer(playerIn, it.extractItem(0, 64, false))
                worldIn.playSound(null, pos,
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f)
            }
        } else {
            var input = ItemHandlerHelper.copyStackWithSize(held, 1)
            val fluidBefore = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))

            if (!FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, blockRayTraceResult.face)) {
                barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, blockRayTraceResult.face).ifPresent {
                    input = ItemHandlerHelper.insertItem(it, input, false)
                }
            } else { //TODO: Sounds don't play when the player is holding a stack of buckets
                val soundEvent: SoundEvent? = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))
                        .map { it.fluid.fillSound }
                        .orElseGet {
                            fluidBefore
                                    .map { it.fluid.emptySound }
                                    .orElse(null)
                        }

                soundEvent?.let { worldIn.playSound(null, pos, it, SoundCategory.NEUTRAL, 1.0f, 1.0f) }
            }

            if (input.isEmpty) {
                val blockFromItem = getBlockFromItem(held.item)

                if (blockFromItem !== Blocks.AIR) {
                    val soundType = blockFromItem.getSoundType(blockFromItem.defaultState, worldIn, pos, playerIn)
                    worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f)
                }

                if (!playerIn.isCreative) {
                    held.shrink(1)
                }
            }
        }

        return true
    }

    override fun getLightValue(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos): Int {
        return super.getLightValue(state, world, pos)
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = BARREL_AABB

    override fun isNormalCube(state: BlockState, world: IBlockReader, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos, face: Direction) = false

    override fun isSolid(state: BlockState) = false

    override fun canEntitySpawn(state: BlockState, worldIn: IBlockReader, pos: BlockPos, type: EntityType<*>) = false

    override fun createTileEntity(state: BlockState, world: IBlockReader) = BarrelTileEntity()

    override fun asItem(): Item {
        if (item == null) {
            item = BlockItem(this, Item.Properties().group(ExNihiloTabs.BARRELS))
        }

        return item!!
    }
}