package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

open class BlockCrucibleRaw(registryName: ResourceLocation, materialIn: Material, soundType: SoundType) : BlockBase(registryName, materialIn, soundType) {
    constructor() : this(LibBlocks.RAW_CRUCIBLE, Material.GROUND, SoundType.GROUND) {
        this.setHardness(1.0f)
    }

    init {
        this.setLightOpacity(1)
    }

    override fun getBlockFaceShape(p_193383_1_: IBlockAccess, p_193383_2_: IBlockState, p_193383_3_: BlockPos, p_193383_4_: EnumFacing): BlockFaceShape {
        return if (p_193383_4_ == EnumFacing.UP) BlockFaceShape.BOWL else BlockFaceShape.UNDEFINED
    }

    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos): Boolean {
        return false
    }

    override fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun isSideSolid(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return false
    }

    override fun canEntitySpawn(state: IBlockState, entityIn: Entity): Boolean {
        return false
    }
}
