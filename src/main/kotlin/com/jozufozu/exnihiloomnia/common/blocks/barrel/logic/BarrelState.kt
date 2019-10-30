package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.block.Block
import net.minecraft.block.Block.NULL_AABB
import net.minecraft.block.BlockState
import net.minecraft.block.state.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityEndermite
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.IEnviromentBlockReader
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.relauncher.OnlyIn
import net.minecraftforge.fml.relauncher.Side
import org.lwjgl.opengl.GL11
import java.util.*

open class BarrelState(val id: ResourceLocation) {
    protected var logic: ArrayList<BarrelLogic> = ArrayList()

    open fun canInteractWithFluids(barrel: BarrelTileEntity): Boolean = true

    open fun canInteractWithItems(barrel: BarrelTileEntity): Boolean = true

    open fun canExtractItems(barrel: BarrelTileEntity): Boolean = false

    fun canExtractFluids(barrel: BarrelTileEntity): Boolean = false

    open fun addCollisionBoxToList(barrel: BarrelTileEntity, state: BlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?) {  }

    open fun getLightValue(barrel: BarrelTileEntity, state: BlockState, world: IEnviromentBlockReader, pos: BlockPos): Int = 0

    open fun activate(barrel: BarrelTileEntity) {
        for (barrelLogic in logic) {
            barrelLogic.onActivate(barrel)
        }
    }

    fun update(barrel: BarrelTileEntity) {
        for (barrelLogic in logic) {
            if (barrelLogic.onUpdate(barrel)) {
                return
            }
        }
    }

    fun canUseItem(barrel: BarrelTileEntity, player: PlayerEntity?, hand: EnumHand?, itemStack: ItemStack): Boolean {
        for (barrelLogic in logic) {
            if (barrelLogic.canUseItem(barrel, player, hand, itemStack)) {
                return true
            }
        }
        return false
    }

    fun onUseItem(barrel: BarrelTileEntity, player: PlayerEntity?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
        for (barrelLogic in logic) {
            val interactResult = barrelLogic.onUseItem(barrel, player, hand, itemStack)
            if (interactResult != EnumInteractResult.PASS) {
                return interactResult
            }
        }

        return EnumInteractResult.PASS
    }

    fun canFillFluid(barrel: BarrelTileEntity, fluidStack: FluidStack): Boolean {
        for (barrelLogic in logic) {
            if (barrelLogic.canFillFluid(barrel, fluidStack)) {
                return true
            }
        }
        return false
    }

    fun onFillFluid(barrel: BarrelTileEntity, fluidStack: FluidStack): EnumInteractResult {
        for (barrelLogic in logic) {
            val interactResult = barrelLogic.onFillFluid(barrel, fluidStack)
            if (interactResult != EnumInteractResult.PASS) {
                return interactResult
            }
        }

        return EnumInteractResult.PASS
    }

    @OnlyIn(Dist.CLIENT)
    open fun draw(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
        if (!isSlaveReady) {
            renderSlave = EntityEndermite(barrel.world)
            renderSlave.isInvisible = true
            renderSlave.isSilent = true
            renderSlave.noClip = true
        }
    }

    @OnlyIn(Dist.CLIENT)
    open fun randomDisplayTick(barrel: BarrelTileEntity, stateIn: BlockState, worldIn: World, pos: BlockPos, rand: Random) {}

    companion object {
        lateinit var renderSlave: EntityLivingBase
        private val isSlaveReady: Boolean get() = ::renderSlave.isInitialized

        val infiniteBoundingBox = AxisAlignedBB(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)

        @JvmStatic fun addCollisionBoxToList(pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, blockBox: AxisAlignedBB?) {
            if (blockBox !== NULL_AABB) {
                val axisalignedbb = blockBox!!.offset(pos)

                if (entityBox.intersects(axisalignedbb)) {
                    collidingBoxes.add(axisalignedbb)
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        fun drawFluid(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
            val fluid = barrel.fluid?.fluid ?: return
            drawFluid(
                    fluid,
                    barrel.fluidAmount.toFloat() / BarrelTileEntity.fluidCapacity.toFloat(),
                    barrel.fluidAmountLastTick.toFloat() / BarrelTileEntity.fluidCapacity.toFloat(),
                    x, y, z, partialTicks,
                    Color(fluid.color),
                    barrel.world.getBlockState(barrel.pos).material.isOpaque)
        }

        @OnlyIn(Dist.CLIENT)
        fun drawFluid(fluid: Fluid, amount: Float, amountLastTick: Float, x: Double, y: Double, z: Double, partialTicks: Float, color: Color, complex: Boolean = false) {
            GlStateManager.pushMatrix()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GlStateManager.enableBlend()
            GlStateManager.disableCull()

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH)
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT)
            }

            Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            val textureMapBlocks = Minecraft.getMinecraft().textureMapBlocks

            val fluidTexture = textureMapBlocks.getAtlasSprite(fluid.still.toString())

            val fullness = MathStuff.lerp(amount, amountLastTick, partialTicks)
            val contentsSize = 0.875 * fullness

            GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125)
            GlStateManager.scale(0.75, 1.0, 0.75)

            RenderHelper.disableStandardItemLighting()

            if (complex) {
                RenderUtil.renderContents(fluidTexture, contentsSize, color)
            } else {
                //TextureAtlasSprite fluidFlow = textureMapBlocks.getAtlasSprite(fluidStack.getFluid().getFlowing(fluidStack).toString());
                RenderUtil.renderContents3D(fluidTexture, fluidTexture, contentsSize, color)
            }


            RenderHelper.enableStandardItemLighting()

            GlStateManager.cullFace(GlStateManager.CullFace.BACK)
            GlStateManager.disableRescaleNormal()
            GlStateManager.disableBlend()

            GlStateManager.popMatrix()
        }

        @OnlyIn(Dist.CLIENT)
        fun drawContents(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
            val contents = barrel.item

            if (contents.isEmpty)
                return

            GlStateManager.pushMatrix()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GlStateManager.enableBlend()

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH)
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT)
            }

            GlStateManager.translate(x + 0.5, y + 0.0625, z + 0.5)

            if (Block.getBlockFromItem(contents.item) !== Blocks.AIR) {
                GlStateManager.translate(0.0, 0.4375, 0.0)
                GlStateManager.scale(0.75, 0.875, 0.75)
            } else {
                GlStateManager.translate(0.0, 0.03125, 0.0)
                GlStateManager.scale(0.75, 0.75, 0.75)
                GlStateManager.rotate(90f, 1f, 0f, 0f)
            }

            Minecraft.getMinecraft().itemRenderer.renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE)

            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}
