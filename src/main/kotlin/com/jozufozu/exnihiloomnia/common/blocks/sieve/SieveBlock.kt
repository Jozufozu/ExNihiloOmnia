package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.client.tileentities.TileEntitySieveRenderer
import com.jozufozu.exnihiloomnia.common.blocks.ModBlock
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.BlockState
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockState
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMultiTexture
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.IBooleanFunction
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.IEnviromentBlockReader
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.OnlyIn
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.items.ItemHandlerHelper

class SieveBlock(name: ResourceLocation, properties: Properties) : ModBlock(name, properties) {
    companion object {
        private val body = VoxelShapes.create(0.0, 9.0 / 16.0, 0.0, 1.0, 13.0 / 16.0, 1.0)
        private val leg = VoxelShapes.create(0.0, 0.0, 0.0, 1.0 / 16.0, 13.5 / 16.0, 1.0 / 16.0)

        private val blockShape: VoxelShape = VoxelShapes.combineAndSimplify(body, leg, IBooleanFunction.OR)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, hand: Hand, rayTrace: BlockRayTraceResult): Boolean {
        val sieve = worldIn.getTileEntity(pos) as? TileEntitySieve ?: return true

        val held = player.getHeldItem(hand)

        if (sieve.hasContents && sieve.hasMesh) {
            sieve.queueWork(player, held)

            val soundType = sieve.blockSound(player)
            worldIn.playSound(null, pos, soundType.hitSound, SoundCategory.BLOCKS, 0.2f, soundType.getPitch() * 0.8f + worldIn.rand.nextFloat() * 0.4f)

            return true
        }

        if (!worldIn.isRemote) {
            if (sieve.hasMesh && !sieve.hasContents) {
                if (player.isSneaking && held.isEmpty) {
                    ItemHandlerHelper.giveItemToPlayer(player, sieve.removeMesh(), player.inventory.currentItem)

                    worldIn.playSound(null, pos,
                            SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f)
                    return true
                }

                if (RegistryManager.siftable(held)) sieve.tryAddContents(player, held)
            } else {
                sieve.trySetMesh(player, held)
            }
        }

        return true
    }

    fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return VoxelShapes.fullCube()
    }

    override fun onReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        (world.getTileEntity(pos) as? TileEntitySieve)?.let {
            spawnAsEntity(world, pos, it.mesh)
        }
        super.onReplaced(state, world, pos, newState, isMoving)
    }

    override fun isNormalCube(p_220081_1_: BlockState, p_220081_2_: IBlockReader, p_220081_3_: BlockPos) = false

    override fun doesSideBlockRendering(state: BlockState?, world: IEnviromentBlockReader?, pos: BlockPos?, face: Direction?) = false

    override fun canEntitySpawn(p_220067_1_: BlockState, p_220067_2_: IBlockReader, p_220067_3_: BlockPos, p_220067_4_: EntityType<*>) = false

    override fun hasTileEntity(state: BlockState) = true

    override fun createTileEntity(state: BlockState?, world: IBlockReader?) = TileEntitySieve()

    @OnlyIn(Dist.CLIENT)
    override fun registerModels() {
        val registryName = this.registryName ?: return
        val itemBlock = itemBlock
        for (enumType in BlockPlanks.EnumType.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.metadata, ModelResourceLocation(registryName, "variant=" + enumType.name))
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve::class.java, TileEntitySieveRenderer())
    }
}
