package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import java.util.*

class BlockKindling : BlockBase(LibBlocks.KINDLING, Material.WOOD, SoundType.WOOD) {
    companion object {
        val DROPS = LootTableList.register(ResourceLocation(ExNihilo.MODID, "gameplay/kindling_drops"))

        val KINDLING_BB = AxisAlignedBB(4.0 / 16.0, 0.0, 4.0 / 16.0, 12.0 / 16.0, 1.0 / 16.0, 12.0 / 16.0)

        val LIT = PropertyBool.create("lit")
    }

    init {
        this.tickRandomly = true
        this.defaultState = this.blockState.baseState.withProperty(LIT, true)
        this.setLightOpacity(0)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (state.getValue(LIT)) return true

        val use = playerIn.getHeldItem(hand)

        if (use.item === Items.FLINT_AND_STEEL) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.withProperty(LIT, true), 3)
                worldIn.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, worldIn.rand.nextFloat() * 0.4f + 0.8f)

                if (!playerIn.isCreative) {
                    use.attemptDamageItem(1, worldIn.rand, playerIn as EntityPlayerMP)
                }
            }
            return true
        }

        if (use.item === Items.STICK) {
            if (!worldIn.isRemote) {
                if (worldIn.rand.nextDouble() <= ModConfig.blocks.kindlingLightChance) {
                    worldIn.setBlockState(pos, state.withProperty(LIT, true), 3)

                    if (!playerIn.isCreative) {
                        use.shrink(1)
                    }
                }

                worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.BLOCKS, 0.4f, 1.0f)
            }
            return true
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote && state.getValue(LIT)) {
            worldIn.setBlockToAir(pos)
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.4f, rand.nextFloat() * 0.3f + 1.1f)

            val nearest = worldIn.getClosestPlayer(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 10.0, false)

            var luck = 1.0f

            if (nearest != null) {
                luck = nearest.luck
            }

            val lootTableManager = worldIn.lootTableManager
            val itemStacks = lootTableManager.getLootTableFromLocation(DROPS)
                    .generateLootForPools(rand, LootContext(luck, worldIn as WorldServer, lootTableManager, null, nearest, null))

            for (itemStack in itemStacks) {
                Block.spawnAsEntity(worldIn, pos, itemStack)
            }
        }
    }

    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        if (!stateIn.getValue(LIT)) return

        if (rand.nextInt(24) == 0) {
            worldIn.playSound((pos.x.toFloat() + 0.5f).toDouble(), (pos.y.toFloat() + 0.5f).toDouble(), (pos.z.toFloat() + 0.5f).toDouble(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.4f, rand.nextFloat() * 0.7f + 0.3f, false)
        }

        for (i in 0..1) {
            val d0 = pos.x.toDouble() + rand.nextDouble() * 0.5 + 0.25
            val d1 = pos.y.toDouble() + rand.nextDouble() * 0.5 + 0.125
            val d2 = pos.z.toDouble() + rand.nextDouble() * 0.5 + 0.25
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0)
        }
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!worldIn.isRemote && !canPlaceBlockAt(worldIn, pos)) worldIn.destroyBlock(pos, true)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        val down = pos.down()
        return worldIn.getBlockState(down).isSideSolid(worldIn, down, EnumFacing.UP)
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int {
        return getMetaFromState(state) * 15
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = KINDLING_BB

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = Block.NULL_AABB

    override fun createBlockState() = BlockStateContainer(this, LIT)

    override fun getStateFromMeta(meta: Int): IBlockState = this.blockState.baseState.withProperty(LIT, meta == 1)

    override fun getMetaFromState(state: IBlockState) = if (state.getValue(LIT)) 1 else 0

    override fun getBlockFaceShape(p_193383_1_: IBlockAccess, p_193383_2_: IBlockState, p_193383_3_: BlockPos, p_193383_4_: EnumFacing): BlockFaceShape =
            BlockFaceShape.UNDEFINED

    override fun getBlockLayer() = BlockRenderLayer.CUTOUT

    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing) = false

    override fun isFullCube(state: IBlockState) = false

    override fun isSideSolid(base_state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing) = false

    override fun canEntitySpawn(state: IBlockState, entityIn: Entity) = false
}
