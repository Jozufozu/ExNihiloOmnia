package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableMap
import net.minecraft.block.Block
import net.minecraft.block.material.EnumPushReaction
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IUnlistedProperty
import java.util.*

open class MimicBlockState(mimic: Block, block: Block) : ExtendedBlockState(block, mimic.blockState.properties.toTypedArray(), (mimic.blockState as? ExtendedBlockState)?.unlistedProperties?.toTypedArray() ?: arrayOf()) {

    private var stateIndex: Int = 0

    private var mimickedToMimicking: HashBiMap<IBlockState, IBlockState>? = null
    private var mimickingToMimicked: HashBiMap<IBlockState, IBlockState>? = null

    fun getMimickedBlockState(mimickingState: IBlockState): IBlockState = mimickingToMimicked?.get(mimickingState) ?: throw BlockMimicException("Cannot map $mimickingState to it's normal state in ${this.block.registryName}")
    fun getMimickingBlockState(stateToMimic: IBlockState): IBlockState = mimickedToMimicking?.get(stateToMimic) ?: throw BlockMimicException("Cannot map $stateToMimic to it's mimicked state in ${this.block.registryName}")

    override fun createState(block: Block, properties: ImmutableMap<IProperty<*>, Comparable<*>>, unlistedProperties: ImmutableMap<IUnlistedProperty<*>, Optional<*>>?): StateImplementation {
        val state = currentMimic.blockState.validStates[stateIndex++]
        val mimic = MimicStateImplementation(block, state)
        if (mimickedToMimicking == null) mimickedToMimicking = HashBiMap.create()
        if (mimickingToMimicked == null) mimickingToMimicked = HashBiMap.create()
        mimickedToMimicking?.put(state, mimic)
        mimickingToMimicked?.put(mimic, state)
        return mimic
    }

    companion object {
        // Oh boy is this hackey
        var currentMimic: Block = Blocks.AIR
    }

    protected class MimicStateImplementation constructor(actual: Block, val mimic: IBlockState) : ExtendedStateImplementation(actual, mimic.properties, (mimic as? ExtendedStateImplementation)?.unlistedProperties ?: ImmutableMap.of(), null, null) {
        override fun isSideSolid(world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = mimic.isSideSolid(world, pos, side)

        override fun getMapColor(p_185909_1_: IBlockAccess, p_185909_2_: BlockPos): MapColor = mimic.getMapColor(p_185909_1_, p_185909_2_)

        override fun getMobilityFlag(): EnumPushReaction = mimic.mobilityFlag

        override fun getMaterial(): Material = mimic.material

        override fun getPlayerRelativeBlockHardness(player: EntityPlayer, worldIn: World, pos: BlockPos): Float = mimic.getPlayerRelativeBlockHardness(player, worldIn, pos)

        override fun getBlockHardness(worldIn: World, pos: BlockPos): Float = mimic.getBlockHardness(worldIn, pos)

        override fun getCollisionBoundingBox(worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB? = mimic.getCollisionBoundingBox(worldIn, pos)

        override fun getSelectedBoundingBox(worldIn: World, pos: BlockPos): AxisAlignedBB = mimic.getSelectedBoundingBox(worldIn, pos)

        override fun getLightOpacity(): Int = mimic.lightOpacity

        override fun getLightOpacity(world: IBlockAccess, pos: BlockPos): Int = mimic.getLightOpacity(world, pos)

        override fun getOffset(access: IBlockAccess, pos: BlockPos): Vec3d = mimic.getOffset(access, pos)

        override fun isFullBlock(): Boolean = mimic.isFullBlock

        override fun doesSideBlockChestOpening(world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = mimic.doesSideBlockChestOpening(world, pos, side)

        override fun canEntitySpawn(entityIn: Entity): Boolean = mimic.canEntitySpawn(entityIn)

        override fun causesSuffocation(): Boolean = mimic.causesSuffocation()

        override fun withMirror(mirrorIn: Mirror): IBlockState = mimic.withMirror(mirrorIn)

        override fun withRotation(rot: Rotation): IBlockState = mimic.withRotation(rot)

        override fun getAmbientOcclusionLightValue(): Float = mimic.ambientOcclusionLightValue

        override fun hasCustomBreakingProgress(): Boolean = mimic.hasCustomBreakingProgress()

        override fun isFullCube(): Boolean = mimic.isFullCube

        override fun canProvidePower(): Boolean = mimic.canProvidePower()

        override fun getBoundingBox(blockAccess: IBlockAccess, pos: BlockPos): AxisAlignedBB = mimic.getBoundingBox(blockAccess, pos)

        override fun getLightValue(): Int = mimic.lightValue

        override fun getLightValue(world: IBlockAccess, pos: BlockPos): Int = mimic.getLightValue(world, pos)

        override fun <T : Comparable<T>?, V : T> withProperty(property: IProperty<T>, value: V): IBlockState = mimic.withProperty(property, value)

        override fun <T : Comparable<T>?> cycleProperty(property: IProperty<T>): IBlockState = mimic.cycleProperty(property)

        override fun isOpaqueCube(): Boolean = mimic.isOpaqueCube

        override fun getProperties(): ImmutableMap<IProperty<*>, Comparable<*>> = mimic.properties

        override fun hasComparatorInputOverride(): Boolean = mimic.hasComparatorInputOverride()

        override fun getComparatorInputOverride(worldIn: World, pos: BlockPos): Int = mimic.getComparatorInputOverride(worldIn, pos)

        override fun shouldSideBeRendered(blockAccess: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean = mimic.shouldSideBeRendered(blockAccess, pos, facing)

        override fun isTranslucent(): Boolean = mimic.isTranslucent

        override fun <T : Comparable<T>?> getValue(property: IProperty<T>): T = mimic.getValue(property)

        override fun addCollisionBoxToList(worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, p_185908_6_: Boolean) = mimic.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_)

        override fun onBlockEventReceived(worldIn: World, pos: BlockPos, id: Int, param: Int): Boolean = mimic.onBlockEventReceived(worldIn, pos, id, param)

        override fun getRenderType(): EnumBlockRenderType = mimic.renderType

        override fun getWeakPower(blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int = mimic.getWeakPower(blockAccess, pos, side)

        override fun isNormalCube(): Boolean = mimic.isNormalCube

        override fun neighborChanged(worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) = mimic.neighborChanged(worldIn, pos, blockIn, fromPos)

        override fun isTopSolid(): Boolean = mimic.isTopSolid

        override fun getBlockFaceShape(p_193401_1_: IBlockAccess, p_193401_2_: BlockPos, p_193401_3_: EnumFacing): BlockFaceShape = mimic.getBlockFaceShape(p_193401_1_, p_193401_2_, p_193401_3_)

        override fun useNeighborBrightness(): Boolean = mimic.useNeighborBrightness()

        override fun getStrongPower(blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int = mimic.getStrongPower(blockAccess, pos, side)

        override fun isBlockNormalCube(): Boolean = mimic.isBlockNormalCube

        override fun getPackedLightmapCoords(source: IBlockAccess, pos: BlockPos): Int = mimic.getPackedLightmapCoords(source, pos)

        override fun doesSideBlockRendering(world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = mimic.doesSideBlockRendering(world, pos, side)
    }
}