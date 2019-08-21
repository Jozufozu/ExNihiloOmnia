package com.jozufozu.exnihiloomnia.client.renderers

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.crucible.CrucibleTileEntity
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import org.lwjgl.opengl.GL11

class TileEntityCrucibleRenderer : TileEntityRenderer<CrucibleTileEntity>() {

    override fun isGlobalRenderer(te: CrucibleTileEntity?): Boolean {
        return true
    }

    override fun render(crucible: CrucibleTileEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (crucible.fluidAmount == 0 && crucible.solidAmount == 0)
            return

        renderSolid(crucible, x, y, z, partialTicks)

//        if (crucible.solidAmount != crucible.fluidAmount || crucible.solidAmount < crucible.solidCapacity)
//            renderFluid(crucible, x, y, z, partialTicks)

        super.render(crucible, x, y, z, partialTicks, destroyStage)
    }

    companion object {
        private const val width = 14.0 / 16.0
        private const val height = 3.0 / 16.0

        fun renderSolid(crucible: CrucibleTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
            val contents = crucible.solidContents

            if (contents.isEmpty)
                return

            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()

            GlStateManager.translated(x + 0.5, y + height, z + 0.5)
            GlStateManager.scaled(width, 1.0, width)

            val solid = crucible.solidAmount.toFloat() / crucible.solidCapacity.toFloat()
            val solidLastTick = crucible.solidAmountLastTick.toFloat() / crucible.solidCapacity.toFloat()

            val fullness = MathStuff.lerp(solidLastTick, solid, partialTicks)
            val contentsSize = 12.5 / 16.0 * fullness

            GlStateManager.translated(0.0, contentsSize / 2.0, 0.0)
            GlStateManager.scaled(1.0, contentsSize, 1.0)

            Minecraft.getInstance().itemRenderer.renderItem(contents, ItemCameraTransforms.TransformType.NONE)

            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }

//        fun renderFluid(crucible: CrucibleTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
//            val fluidStack = crucible.fluidContents ?: return
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
//            val fluidTexture = Minecraft.getInstance().textureMap.getAtlasSprite(fluidStack.fluid.still.toString())
//
//            val fluid = (crucible.fluidAmount + crucible.partialFluid) / crucible.fluidCapacity.toFloat()
//            val fluidLastTick = (crucible.fluidAmountLastTick + crucible.partialFluidLastTick) / crucible.fluidCapacity.toFloat()
//            val fullness = MathStuff.lerp(fluid, fluidLastTick, partialTicks)
//            val contentsSize = 12.5 / 16.0 * fullness
//
//            GlStateManager.translated(x + 1.0 / 16.0, y + height, z + 1.0 / 16.0)
//            GlStateManager.scaled(width, 1.0, width)
//
//            RenderHelper.disableStandardItemLighting()
//            RenderUtil.renderContents(fluidTexture, contentsSize, Color.WHITE)
//            RenderHelper.enableStandardItemLighting()
//
//            GlStateManager.cullFace(GlStateManager.CullFace.BACK)
//            GlStateManager.disableRescaleNormal()
//            GlStateManager.disableBlend()
//
//            GlStateManager.popMatrix()
//        }
    }
}
