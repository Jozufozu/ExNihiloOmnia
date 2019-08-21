package com.jozufozu.exnihiloomnia.client.renderers

import com.jozufozu.exnihiloomnia.common.blocks.sieve.SieveTileEntity
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class SieveTileEntityRenderer : TileEntityRenderer<SieveTileEntity>() {

    override fun render(sieve: SieveTileEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        super.render(sieve, x, y, z, partialTicks, destroyStage)

        val contents = sieve.contents
        val mesh = sieve.mesh

        if (!mesh.isEmpty) {
            GlStateManager.pushMatrix()

            GlStateManager.translated(x + 0.5, y + height, z + 0.5)
            GlStateManager.scaled(scale, 0.2, scale)
            GlStateManager.rotated(90.0, 1.0, 0.0, 0.0)

            Minecraft.getInstance().itemRenderer.renderItem(mesh, ItemCameraTransforms.TransformType.NONE)
        }

        if (!contents.isEmpty) {
            val progress = MathStuff.lerp(sieve.countdownLastTick.toDouble() / sieve.requiredTime, sieve.countdown.toDouble() / sieve.requiredTime, partialTicks.toDouble())

            val contentsSize = 7.0 / 16.0 * progress

            GlStateManager.rotated(-90.0, 1.0, 0.0, 0.0)

            GlStateManager.scaled(1.0, 5.0, 1.0)
            GlStateManager.translated(0.0, contentsSize / 2.0, 0.0)
            GlStateManager.scaled(1.0, contentsSize, 1.0)

            Minecraft.getInstance().itemRenderer.renderItem(contents, ItemCameraTransforms.TransformType.NONE)
        }

        GlStateManager.popMatrix()
    }

    companion object {
        private const val scale = 14.0 / 16.0
        private const val height = 9.0 / 16.0
    }
}
