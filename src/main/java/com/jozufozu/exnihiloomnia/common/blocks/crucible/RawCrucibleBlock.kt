package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlock
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.EntityType
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IEnviromentBlockReader
import java.util.*

open class RawCrucibleBlock(registryName: ResourceLocation, properties: Properties) : ExNihiloBlock(registryName, properties) {
    constructor() : this(BlocksLib.RAW_CRUCIBLE, Properties.from(Blocks.CLAY))

    override fun isNormalCube(p_220081_1_: BlockState, p_220081_2_: IBlockReader, p_220081_3_: BlockPos) = false

    override fun isSolid(p_200124_1_: BlockState) = false

    override fun doesSideBlockRendering(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos, face: Direction) = false

    override fun canCreatureSpawn(state: BlockState, world: IBlockReader, pos: BlockPos, type: EntitySpawnPlacementRegistry.PlacementType, entityType: EntityType<*>?) = false
}
