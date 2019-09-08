package com.jozufozu.exnihiloomnia.common.blocks.crucible

import com.jozufozu.exnihiloomnia.client.tileentities.TileEntityCrucibleRenderer
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

class BlockCrucible : BlockCrucibleRaw(LibBlocks.CRUCIBLE, Material.ROCK, SoundType.STONE), ITileEntityProvider {
    init {
        this.setLightOpacity(1)
        this.setHardness(3.0f)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote) (worldIn.getTileEntity(pos) as? TileEntityCrucible)?.let {
            val held = playerIn.getHeldItem(hand)

            var toInsert = ItemHandlerHelper.copyStackWithSize(held, 1)

            if (FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                val fluidContained = FluidUtil.getFluidContained(playerIn.getHeldItem(hand))

                if (fluidContained != null) {
                    val fluid = fluidContained.fluid
                    worldIn.playSound(null, pos, fluid.getFillSound(fluidContained), SoundCategory.NEUTRAL, 1.0f, 1.0f)
                }
            } else {
                toInsert = ItemHandlerHelper.insertItem(it.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing), toInsert, false)
            }

            if (toInsert.isEmpty) {
                val blockFromItem = getBlockFromItem(held.item)

                if (blockFromItem !== Blocks.AIR) {
                    val soundType = blockFromItem.getSoundType(blockFromItem.getStateFromMeta(held.metadata), worldIn, pos, playerIn)
                    worldIn.playSound(null, pos, soundType.placeSound, SoundCategory.BLOCKS, 0.2f + worldIn.rand.nextFloat() * 0.2f, 1.1f + worldIn.rand.nextFloat() * 0.4f)
                }
                if (!playerIn.isCreative) held.shrink(1)
            }
        }

        return true
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        return TileEntityCrucible()
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        super.registerModels()
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible::class.java, TileEntityCrucibleRenderer())
    }
}
