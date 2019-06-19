package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.lib.BlocksLib
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.material.PushReaction
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.Items
import net.minecraft.particles.ParticleTypes
import net.minecraft.state.BooleanProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.*
import net.minecraft.world.storage.loot.LootContext
import net.minecraft.world.storage.loot.LootTables
import java.util.*

class KindlingBlock : ExNihiloBlock(BlocksLib.KINDLING, Properties.create(Material.WOOD).sound(SoundType.WOOD)) {
    companion object {
        @JvmStatic val DROPS: ResourceLocation = LootTables.register(ResourceLocation(ExNihilo.MODID, "exnihiloomnia/kindling_drops"))

        @JvmStatic val KINDLING_BB: VoxelShape = makeCuboidShape(4.0 / 16.0, 0.0, 4.0 / 16.0, 12.0 / 16.0, 1.0 / 16.0, 12.0 / 16.0)

        val LIT: BooleanProperty = BooleanProperty.create("lit")
    }

    init {
        this.defaultState = this.stateContainer.baseState.with(LIT, true)
    }

    override fun onBlockActivated(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): Boolean {
        if (state.get(LIT)) return true

        val use = player.getHeldItem(hand)

        if (use.item === Items.FLINT_AND_STEEL) {
            if (!world.isRemote) {
                world.setBlockState(pos, state.with(LIT, true), 3)
                world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.rand.nextFloat() * 0.4f + 0.8f)

                if (!player.isCreative) {
                    use.attemptDamageItem(1, world.rand, player as ServerPlayerEntity)
                }
            }
            return true
        }

        if (use.item === Items.STICK) {
            if (!world.isRemote) {
                if (world.rand.nextDouble() <= ModConfig.blocks.kindlingLightChance) {
                    world.setBlockState(pos, state.with(LIT, true), 3)

                    if (!player.isCreative) {
                        use.shrink(1)
                    }
                }

                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 0.4f, 1.0f)
            }
            return true
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit)
    }

    override fun tick(state: BlockState, worldIn: World, pos: BlockPos, random: Random) {
        if (!worldIn.isRemote && state.get(LIT)) {
            worldIn.removeBlock(pos, false)
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2f + random.nextFloat() * 0.4f, random.nextFloat() * 0.3f + 1.1f)

            val nearest = worldIn.getClosestPlayer(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 10.0, false)

            (worldIn as? ServerWorld)?.let {
                val builder = LootContext.Builder(it)
                        .withLuck(nearest?.luck ?: 1.0f)
                val itemStacks = it.server.lootTableManager.getLootTableFromLocation(DROPS).generate(builder.build())

                for (itemStack in itemStacks) {
                    spawnAsEntity(worldIn, pos, itemStack)
                }
            }
        }
    }

    override fun animateTick(stateIn: BlockState, worldIn: World, pos: BlockPos, rand: Random) {
        if (!stateIn.get(LIT)) return

        if (rand.nextInt(24) == 0) {
            worldIn.playSound((pos.x.toFloat() + 0.5f).toDouble(), (pos.y.toFloat() + 0.5f).toDouble(), (pos.z.toFloat() + 0.5f).toDouble(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.4f, rand.nextFloat() * 0.7f + 0.3f, false)
        }

        for (i in 0..1) {
            val x = pos.x.toDouble() + rand.nextDouble() * 0.5 + 0.25
            val y = pos.y.toDouble() + rand.nextDouble() * 0.5 + 0.125
            val z = pos.z.toDouble() + rand.nextDouble() * 0.5 + 0.25
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    override fun getPushReaction(state: BlockState) = PushReaction.DESTROY

    override fun onNeighborChange(state: BlockState, world: IWorldReader, pos: BlockPos, neighbor: BlockPos) {
        if (!world.isRemote && !canPlaceBlockAt(world, pos)) world.destroyBlock(pos, true)
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean {
        return worldIn.getBlockState(pos.down()).isSolid
    }

    override fun getLightValue(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos) = if (state.get(LIT)) 15 else 0

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = KINDLING_BB

    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = VoxelShapes.empty()

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(LIT)
    }

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: BlockState, world: IEnviromentBlockReader, pos: BlockPos, face: Direction) = false

    override fun isSolid(state: BlockState) = false

    override fun canEntitySpawn(state: BlockState, worldIn: IBlockReader, pos: BlockPos, type: EntityType<*>) = false
}
