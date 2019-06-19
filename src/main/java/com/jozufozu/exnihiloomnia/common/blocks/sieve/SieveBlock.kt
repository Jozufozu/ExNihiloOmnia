package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlock
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.BlockState
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.IBooleanFunction
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.*

class SieveBlock(registryName: ResourceLocation, properties: Properties) : ExNihiloBlock(registryName, properties) {
    companion object {
        private val FRAME_BOX: VoxelShape = VoxelShapes.create(0.0, 9.0, 0.0, 16.0, 13.0, 16.0)
        private val FRAME_MIDDLE: VoxelShape = VoxelShapes.create(1.0, 9.0, 1.0, 15.0, 13.0, 15.0)

        private val SIEVE_SHAPE: VoxelShape = VoxelShapes.combineAndSimplify(FRAME_BOX, FRAME_MIDDLE, IBooleanFunction.ONLY_FIRST)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): Boolean {
        val sieve = worldIn.getTileEntity(pos) as? SieveTileEntity ?: return true

        val held = player.getHeldItem(hand)

        if (sieve.hasContents() && sieve.hasMesh()) {
            sieve.queueWork(player, held)

            val soundType = sieve.blockSound(player)
            worldIn.playSound(null, pos, soundType.hitSound, SoundCategory.BLOCKS, 0.2f, soundType.getPitch() * 0.8f + worldIn.rand.nextFloat() * 0.4f)
        } else if (!worldIn.isRemote) {
            if (!sieve.hasMesh()) {
                sieve.trySetMesh(player, held)
            } else {
                if (player.isSneaking && held.isEmpty) {
                    sieve.removeMesh(player)
                    worldIn.playSound(null, pos,
                            SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f)
                    return true
                }

                if (RegistryManager.siftable(held)) sieve.insertContents(player, held)
            }
        }

        return true
    }

    override fun onPlayerDestroy(worldIn: IWorld, pos: BlockPos, state: BlockState) {
        (worldIn.getTileEntity(pos) as? SieveTileEntity)?.let {
            if (worldIn is ServerWorld && !it.mesh.isEmpty && worldIn.gameRules.getBoolean("doTileDrops") && !worldIn.restoringBlockSnapshots) { // do not drop items while restoring blockstates, prevents item dupe
                val x = worldIn.rand.nextFloat() * 0.5 + 0.25
                val y = worldIn.rand.nextFloat() * 0.5 + 0.25
                val z = worldIn.rand.nextFloat() * 0.5 + 0.25

                with(ItemEntity(worldIn, pos.x + x, pos.y + y, pos.z + z, it.mesh)) {
                    setDefaultPickupDelay()
                    worldIn.addEntity(this)
                }
            }
        }

        super.onPlayerDestroy(worldIn, pos, state)
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SIEVE_SHAPE

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos, face: Direction) = false

    override fun isSolid(state: BlockState) = false

    override fun canCreatureSpawn(state: BlockState, world: IBlockReader, pos: BlockPos, type: EntitySpawnPlacementRegistry.PlacementType, entityType: EntityType<*>?) = false

    override fun hasTileEntity(state: BlockState) = true

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = SieveTileEntity()
}
