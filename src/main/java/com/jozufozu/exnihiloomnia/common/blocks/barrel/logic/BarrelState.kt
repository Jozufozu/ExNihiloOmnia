package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.lwjgl.opengl.GL11
import java.util.*

open class BarrelState(val id: ResourceLocation) {
    protected var logic = ArrayList<BarrelLogic>()

    open fun canInteractWithFluids(barrel: TileEntityBarrel) = true

    open fun canInteractWithItems(barrel: TileEntityBarrel) = true

    open fun canExtractItems(barrel: TileEntityBarrel) = false

    //open fun canExtractFluids(barrel: TileEntityBarrel) = false

    open fun activate(barrel: TileEntityBarrel, world: World, previousState: BarrelState) {
        for (barrelLogic in logic) {
            barrelLogic.onActivate(barrel, world, previousState)
        }
    }

    fun update(barrel: TileEntityBarrel, world: World) {
        for (barrelLogic in logic) {
            if (barrelLogic.onUpdate(barrel, world)) {
                return
            }
        }
    }

    fun canUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): Boolean {
        for (barrelLogic in logic) {
            if (barrelLogic.canUseItem(barrel, world, itemStack, player, hand)) {
                return true
            }
        }
        return false
    }

    fun onUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): InteractResult {
        for (barrelLogic in logic) {
            val interactResult = barrelLogic.onUseItem(barrel, world, itemStack, player, hand)
            if (interactResult != InteractResult.PASS) {
                barrel.sync(true)
                return interactResult
            }
        }

        return InteractResult.PASS
    }

//    fun canFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack): Boolean {
//        for (barrelLogic in logic) {
//            if (barrelLogic.canFillFluid(barrel, world, fluidStack)) {
//                return true
//            }
//        }
//        return false
//    }
//
//    fun onFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack): InteractResult {
//        for (barrelLogic in logic) {
//            val interactResult = barrelLogic.onFillFluid(barrel, world, fluidStack)
//            if (interactResult != InteractResult.PASS) {
//                barrel.sync(true)
//                return interactResult
//            }
//        }
//
//        return InteractResult.PASS
//    }

    @OnlyIn(Dist.CLIENT)
    open fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) { }

    companion object {
//        @OnlyIn(Dist.CLIENT)
//        fun renderFluid(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
//            val fluidStack = barrel.fluid ?: return
//
//            GlStateManager.pushMatrix()
//            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
//            GlStateManager.enableBlend()
//            GlStateManager.disableCull()
//
//            if (Minecraft.isAmbientOcclusionEnabled()) {
//                GlStateManager.shadeModel(GL11.GL_SMOOTH)
//            } else {
//                GlStateManager.shadeModel(GL11.GL_FLAT)
//            }
//
//            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
//            val textureMapBlocks = Minecraft.getInstance().textureMap
//
//            val fluidTexture = textureMapBlocks.getAtlasSprite(fluidStack.fluid.getStill(fluidStack).toString())
//
//            val fluid = barrel.fluidAmount.toFloat() / barrel.fluidCapacity.toFloat()
//            val fluidLastTick = barrel.fluidAmountLastTick.toFloat() / barrel.fluidCapacity.toFloat()
//            val fullness = MathStuff.lerp(fluid, fluidLastTick, partialTicks)
//            val contentsSize = 0.875 * fullness
//
//            GlStateManager.translated(x + 0.125, y + 0.0625, z + 0.125)
//            GlStateManager.scaled(0.75, 1.0, 0.75)
//
//            RenderHelper.disableStandardItemLighting()
//
//            if (barrel.world!!.getBlockState(barrel.pos).material.isOpaque) {
//                RenderUtil.renderContents(fluidTexture, contentsSize, Color(fluidStack.fluid.getColor(fluidStack)))
//            } else {
//                //TextureAtlasSprite fluidFlow = textureMapBlocks.getAtlasSprite(fluidStack.getFluid().getFlowing(fluidStack).toString());
//                RenderUtil.renderContents3D(fluidTexture, fluidTexture, contentsSize, Color(fluidStack.fluid.getColor(fluidStack)))
//            }
//
//
//            RenderHelper.enableStandardItemLighting()
//
//            GlStateManager.cullFace(GlStateManager.CullFace.BACK)
//            GlStateManager.disableRescaleNormal()
//            GlStateManager.disableBlend()
//
//            GlStateManager.popMatrix()
//        }

        @OnlyIn(Dist.CLIENT)
        fun renderContents(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
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

            GlStateManager.translated(x + 0.5, y + 0.0625, z + 0.5)

            if (Block.getBlockFromItem(contents.item) !== Blocks.AIR) {
                GlStateManager.translated(0.0, 0.4375, 0.0)
                GlStateManager.scaled(0.75, 0.875, 0.75)
            } else {
                GlStateManager.translated(0.0, 0.03125, 0.0)
                GlStateManager.scaled(0.75, 0.75, 0.75)
                GlStateManager.rotated(90.0, 1.0, 0.0, 0.0)
            }

            Minecraft.getInstance().itemRenderer.renderItem(contents, ItemCameraTransforms.TransformType.NONE)

            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}
