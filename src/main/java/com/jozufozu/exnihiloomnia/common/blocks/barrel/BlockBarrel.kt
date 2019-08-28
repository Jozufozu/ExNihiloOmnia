package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.client.renderers.TileEntityBarrelRenderer
import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
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
import net.minecraft.init.Blocks
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
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

open class BlockBarrel @JvmOverloads constructor(registryName: ResourceLocation, materialIn: Material, soundType: SoundType = SoundType.STONE) : BlockBase(registryName, materialIn, soundType), ITileEntityProvider {
    init {
        this.setCreativeTab(ExNihiloTabs.BARRELS)
        this.fullBlock = false
        this.setLightOpacity(1)
    }

    companion object {
        @JvmField val BARREL_AABB = AxisAlignedBB(1.0 / 16.0, 0.0, 1.0 / 16.0, 15.0 / 16.0, 1.0, 15.0 / 16.0)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        val barrel = worldIn.getTileEntity(pos) as? TileEntityBarrel ?: return true

        val held = playerIn.getHeldItem(hand)

        if (barrel.state.canExtractItems(barrel) && playerIn.isSneaking && held.isEmpty) {
            val barrelItems = barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)

            if (barrelItems != null) {
                ItemHandlerHelper.giveItemToPlayer(playerIn, barrelItems.extractItem(0, 64, false))
                worldIn.playSound(null, pos,
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f)
            }
        } else {
            var input = ItemHandlerHelper.copyStackWithSize(held, 1)
            val fluidBefore = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))

            if (!FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                input = ItemHandlerHelper.insertItem(barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing), input, false)
            } else
            //TODO: Sounds don't play when the play is holding a stack of buckets
            {
                val fluidContained = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))

                if (fluidContained != null) {
                    val fluid = fluidContained.fluid
                    worldIn.playSound(null, pos, fluid.getFillSound(fluidContained), SoundCategory.NEUTRAL, 1.0f, 1.0f)
                } else if (fluidBefore != null) {
                    val fluid = fluidBefore.fluid
                    worldIn.playSound(null, pos, fluid.getEmptySound(fluidBefore), SoundCategory.NEUTRAL, 1.0f, 1.0f)
                }
            }

            if (input.isEmpty) {
                val blockFromItem = Block.getBlockFromItem(held.item)

                if (blockFromItem !== Blocks.AIR) {
                    val soundType = blockFromItem.getSoundType(blockFromItem.getStateFromMeta(held.metadata), worldIn, pos, playerIn)
                    worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f)
                }

                if (!playerIn.isCreative) {
                    held.shrink(1)
                }
            }
        }

        return true
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = BARREL_AABB

    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false

    override fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing) = false

    override fun isFullCube(state: IBlockState) = false

    override fun isSideSolid(base_state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing) = false

    override fun canEntitySpawn(state: IBlockState, entityIn: Entity) = false

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityBarrel()

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        super.registerModels()
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel::class.java, TileEntityBarrelRenderer())
    }
}
