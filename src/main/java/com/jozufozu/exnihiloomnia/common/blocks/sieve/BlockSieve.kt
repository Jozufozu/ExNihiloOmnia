package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.client.renderers.TileEntitySieveRenderer
import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMultiTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraft.world.storage.loot.LootContext
import net.minecraft.world.storage.loot.LootTableList
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class BlockSieve : BlockBase(LibBlocks.SIEVE, Material.WOOD), ITileEntityProvider, IModelRegister {
    companion object {
        @JvmField val VARIANT: PropertyEnum<BlockPlanks.EnumType> = BlockPlanks.VARIANT

        private val SIEVE_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 13.0 / 16.0, 1.0)
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty(VARIANT, BlockPlanks.EnumType.OAK)
        this.fullBlock = false
        this.setLightOpacity(1)
        this.setHardness(2.0f)
        this.setResistance(3.0f)
        this.soundType = SoundType.WOOD
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val sieve = worldIn.getTileEntity(pos) as? TileEntitySieve ?: return true

        val held = playerIn.getHeldItem(hand)

        if (sieve.hasContents() && sieve.hasMesh()) {
            sieve.queueWork(playerIn, held)

            val soundType = sieve.blockSound(playerIn)
            worldIn.playSound(null, pos, soundType.hitSound, SoundCategory.BLOCKS, 0.2f, soundType.getPitch() * 0.8f + worldIn.rand.nextFloat() * 0.4f)
        } else if (!worldIn.isRemote) {
            if (!sieve.hasMesh()) {
                sieve.trySetMesh(playerIn, held)
            } else {
                if (playerIn.isSneaking && held == ItemStack.EMPTY) {
                    sieve.removeMesh(playerIn)
                    worldIn.playSound(null, pos,
                            SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f)
                    return true
                }

                if (RegistryManager.siftable(held)) sieve.insertContents(playerIn, held)
            }
        }

        return true
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val sieve = worldIn.getTileEntity(pos) as? TileEntitySieve

        if (sieve != null) {
            spawnAsEntity(worldIn, pos, sieve.mesh)
        }

        super.breakBlock(worldIn, pos, state)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return ItemStack(this, 1, getMetaFromState(state))
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = SIEVE_AABB

    override fun getBlockFaceShape(p_193383_1_: IBlockAccess, p_193383_2_: IBlockState, p_193383_3_: BlockPos, p_193383_4_: EnumFacing) = BlockFaceShape.UNDEFINED

    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing) = false

    override fun isFullCube(state: IBlockState) = false

    override fun isSideSolid(base_state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing) = false

    override fun canEntitySpawn(state: IBlockState, entityIn: Entity) = false

    override fun getSubBlocks(itemIn: CreativeTabs, items: NonNullList<ItemStack>) {
        items.addAll(BlockPlanks.EnumType.values().map { ItemStack(this, 1, it.metadata) })
    }

    override fun createBlockState() = BlockStateContainer(this, VARIANT)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.blockState.baseState.withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState) = state.getValue(VARIANT).metadata

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntitySieve()

    override val itemBlock: ItemBlock by lazy {
        ItemMultiTexture(this, this) { item -> BlockPlanks.EnumType.byMetadata(item.metadata).unlocalizedName }.also {
            it.registryName = registryName
        }
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val registryName = this.registryName ?: return
        val itemBlock = itemBlock
        for (enumType in BlockPlanks.EnumType.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.metadata, ModelResourceLocation(registryName, "variant=" + enumType.name))
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve::class.java, TileEntitySieveRenderer())
    }
}
