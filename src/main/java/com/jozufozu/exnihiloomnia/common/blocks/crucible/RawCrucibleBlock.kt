package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlock
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos

open class RawCrucibleBlock(registryName: ResourceLocation, materialIn: Material, soundType: SoundType) : ExNihiloBlock(registryName, materialIn, soundType) {
    constructor() : this(BlocksLib.RAW_CRUCIBLE, Material.GROUND, SoundType.GROUND) {
        this.setHardness(1.0f)
    }

    init {
        this.setLightOpacity(1)
    }

    fun getBlockFaceShape(p_193383_1_: IBlockAccess, p_193383_2_: IBlockState, p_193383_3_: BlockPos, p_193383_4_: EnumFacing): BlockFaceShape {
        return if (p_193383_4_ === EnumFacing.UP) BlockFaceShape.BOWL else BlockFaceShape.UNDEFINED
    }

    fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos): Boolean {
        return false
    }

    fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean {
        return false
    }

    fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    fun isSideSolid(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return false
    }

    fun canEntitySpawn(state: IBlockState, entityIn: Entity): Boolean {
        return false
    }
}
