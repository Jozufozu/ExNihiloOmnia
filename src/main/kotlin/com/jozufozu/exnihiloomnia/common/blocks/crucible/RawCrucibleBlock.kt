package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.blocks.ModBlock
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

open class RawCrucibleBlock(registryName: ResourceLocation, materialIn: Material, soundType: SoundType) : ModBlock(registryName, materialIn, soundType) {
    constructor() : this(BlocksLib.RAW_CRUCIBLE, Material.GROUND, SoundType.GROUND) {
        this.setHardness(1.0f)
    }

    init {
        this.setLightOpacity(1)
    }

    override fun getBlockFaceShape(p_193383_1_: IBlockAccess, p_193383_2_: BlockState, p_193383_3_: BlockPos, enumFacing: EnumFacing): BlockFaceShape {
        return when (enumFacing) {
            EnumFacing.UP -> BlockFaceShape.BOWL
            in EnumFacing.HORIZONTALS -> BlockFaceShape.SOLID
            else -> BlockFaceShape.UNDEFINED
        }
    }

    override fun isNormalCube(state: BlockState, world: IBlockAccess, pos: BlockPos): Boolean {
        return false
    }

    override fun doesSideBlockRendering(state: BlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean {
        return face == EnumFacing.UP
    }

    override fun isFullCube(state: BlockState): Boolean {
        return false
    }

    override fun isSideSolid(state: BlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return side in EnumFacing.HORIZONTALS
    }

    override fun canEntitySpawn(state: BlockState, entityIn: Entity): Boolean {
        return false
    }

    override fun addCollisionBoxToList(state: BlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, p_185477_7_: Boolean) {
        for (box in collisionBoxes) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box)
        }
    }

    companion object {
        val collisionBoxes = arrayOf(
                AxisAlignedBB(0.0, 2.0 / 16.0, 1.0 / 16.0, 1.0, 3.0 / 16.0, 1.0),
                AxisAlignedBB(0.0, 1.0 / 16.0, 1.0 / 16.0, 1.0 / 16.0, 1.0, 1.0),
                AxisAlignedBB(0.0, 1.0 / 16.0, 1.0 / 16.0, 1.0, 1.0, 1.0 / 16.0),
                AxisAlignedBB(1.0, 1.0 / 16.0, 1.0 / 16.0, 1.0, 1.0, 1.0),
                AxisAlignedBB(0.0, 1.0 / 16.0, 15.0 / 16.0, 1.0, 1.0, 1.0)
        )
    }
}
