package com.jozufozu.exnihiloomnia.client.renderers

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityGuardian
import org.lwjgl.opengl.GL11

class TileEntityCrucibleRenderer : TileEntitySpecialRenderer<TileEntityCrucible>() {

    override fun isGlobalRenderer(te: TileEntityCrucible?): Boolean {
        return true
    }

    override fun render(crucible: TileEntityCrucible, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        if (renderSlave == null)
            renderSlave = EntityGuardian(world)

        if (crucible.fluidAmount == 0 && crucible.solidAmount == 0)
            return

        renderSolid(crucible, x, y, z, partialTicks)

        if (crucible.solidAmount != crucible.fluidAmount || crucible.solidAmount < crucible.solidCapacity)
            renderFluid(crucible, x, y, z, partialTicks)
    }

    companion object {
        private var renderSlave: EntityLivingBase? = null

        private val width = 14.0 / 16.0
        private val height = 3.0 / 16.0

        fun renderSolid(crucible: TileEntityCrucible, x: Double, y: Double, z: Double, partialTicks: Float) {
            val contents = crucible.solidContents

            if (contents.isEmpty)
                return

            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()

            GlStateManager.translate(x + 0.5, y + height, z + 0.5)
            GlStateManager.scale(width, 1.0, width)

            val solid = crucible.solidAmount.toFloat() / crucible.solidCapacity.toFloat()
            val solidLastTick = crucible.solidAmountLastTick.toFloat() / crucible.solidCapacity.toFloat()

            val fullness = MathStuff.lerp(solidLastTick, solid, partialTicks)
            val contentsSize = 12.5 / 16.0 * fullness

            GlStateManager.translate(0.0, contentsSize / 2.0, 0.0)
            GlStateManager.scale(1.0, contentsSize, 1.0)

            Minecraft.getMinecraft().itemRenderer.renderItem(renderSlave!!, contents, ItemCameraTransforms.TransformType.NONE)

            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }

        fun renderFluid(crucible: TileEntityCrucible, x: Double, y: Double, z: Double, partialTicks: Float) {
            val fluidStack = crucible.fluidContents ?: return

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
            val fluidTexture = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(fluidStack.fluid.still.toString())

            val fluid = (crucible.fluidAmount + crucible.partialFluid) / crucible.fluidCapacity.toFloat()
            val fluidLastTick = (crucible.fluidAmountLastTick + crucible.partialFluidLastTick) / crucible.fluidCapacity.toFloat()
            val fullness = MathStuff.lerp(fluid, fluidLastTick, partialTicks)
            val contentsSize = 12.5 / 16.0 * fullness

            GlStateManager.translate(x + 1.0 / 16.0, y + height, z + 1.0 / 16.0)
            GlStateManager.scale(width, 1.0, width)

            RenderHelper.disableStandardItemLighting()
            RenderUtil.renderContents(fluidTexture, contentsSize, Color.WHITE)
            RenderHelper.enableStandardItemLighting()

            GlStateManager.cullFace(GlStateManager.CullFace.BACK)
            GlStateManager.disableRescaleNormal()
            GlStateManager.disableBlend()

            GlStateManager.popMatrix()
        }
    }
}
