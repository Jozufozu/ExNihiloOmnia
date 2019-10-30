package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.client.tileentities.TileEntityCrucibleRenderer
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.OnlyIn
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

class CrucibleBlock : RawCrucibleBlock(BlocksLib.CRUCIBLE, Material.ROCK, SoundType.STONE), ITileEntityProvider {
    init {
        this.setLightOpacity(1)
        this.setHardness(3.0f)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: BlockState, playerIn: PlayerEntity, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote) (worldIn.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            val held = playerIn.getHeldItem(hand)

            var toInsert = ItemHandlerHelper.copyStackWithSize(held, 1)

            if (FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                val fluidContained = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))

                if (fluidContained != null) {
                    val fluid = fluidContained.fluid
                    worldIn.playSound(null, pos, fluid.getFillSound(fluidContained), SoundCategory.NEUTRAL, 1.0f, 1.0f)
                }
            } else {
                toInsert = ItemHandlerHelper.insertItem(it.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing), toInsert, false)
            }

            if (toInsert.isEmpty) {
                val blockFromItem = getBlockFromItem(held.item)

                if (blockFromItem !== Blocks.AIR) {
                    val soundType = blockFromItem.getSoundType(blockFromItem.getStateFromMeta(held.metadata), worldIn, pos, playerIn)
                    worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f)
                }
                if (!playerIn.isCreative) held.shrink(1)
            }
        }

        return true
    }

    override fun addCollisionBoxToList(state: BlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, p_185477_7_: Boolean) {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_)

        (worldIn.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, it.solidBB)
        }
    }

    override fun isAABBInsideMaterial(world: World, pos: BlockPos, boundingBox: AxisAlignedBB, material: Material): Boolean {
        (world.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            it.fluidContents?.let { fluid ->
                if (it.fluidBB.offset(pos).intersects(boundingBox)) {
                    return fluid.fluid.block.defaultState.material == material
                }
            }
        }

        return false
    }

    override fun isEntityInsideMaterial(world: IBlockAccess, pos: BlockPos, state: BlockState, entity: Entity, yToTest: Double, material: Material, testingHead: Boolean): Boolean {
        (world.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            it.fluidContents?.let { fluid ->
                if (it.fluidBB.offset(pos).intersects(entity.entityBoundingBox)) {
                    return fluid.fluid.block.defaultState.material == material
                }
            }
        }

        return false
    }

    override fun isAABBInsideLiquid(world: World, pos: BlockPos, boundingBox: AxisAlignedBB): Boolean {
        (world.getTileEntity(pos) as? CrucibleTileEntity)?.let {
            return it.fluidBB.offset(pos).intersects(boundingBox)
        }

        return false
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = CrucibleTileEntity()

    @OnlyIn(Dist.CLIENT)
    override fun registerModels() {
        super.registerModels()
        ClientRegistry.bindTileEntitySpecialRenderer(CrucibleTileEntity::class.java, TileEntityCrucibleRenderer())
    }
}
