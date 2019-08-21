package com.jozufozu.exnihiloomnia.client

import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11

object RenderUtil {
    /**
     * Draws a flat texture on the xz plane from (0,0) to (1,1). Be sure to translate and scale to your needs
     */
    fun renderContents(texture: TextureAtlasSprite, height: Double, color: Color) {
        val minU = texture.minU.toDouble()
        val maxU = texture.maxU.toDouble()
        val minV = texture.minV.toDouble()
        val maxV = texture.maxV.toDouble()

        val tessellator = Tessellator.getInstance()
        val renderer = tessellator.buffer

        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR)
        renderer.pos(1.0, height, 1.0).tex(maxU, maxV).color(color.r, color.g, color.b, color.a).endVertex()
        renderer.pos(1.0, height, 0.0).tex(maxU, minV).color(color.r, color.g, color.b, color.a).endVertex()
        renderer.pos(0.0, height, 0.0).tex(minU, minV).color(color.r, color.g, color.b, color.a).endVertex()
        renderer.pos(0.0, height, 1.0).tex(minU, maxV).color(color.r, color.g, color.b, color.a).endVertex()
        tessellator.draw()
    }

    fun renderContents3D(topTexture: TextureAtlasSprite, sideTexture: TextureAtlasSprite, height: Double, color: Color) {
        renderContents3D(topTexture, sideTexture, topTexture, height, color)
    }

    fun renderContents3D(topTexture: TextureAtlasSprite, sideTexture: TextureAtlasSprite, bottomTexture: TextureAtlasSprite, height: Double, color: Color) {
        val vertices = arrayOf(Vec3d(1.0, height, 1.0), Vec3d(1.0, height, 0.0), Vec3d(0.0, height, 0.0), Vec3d(0.0, height, 1.0), Vec3d(0.0, 0.0, 1.0), Vec3d(0.0, 0.0, 0.0), Vec3d(1.0, 0.0, 0.0), Vec3d(1.0, 0.0, 1.0))
        val top = arrayOf(vertices[0], vertices[1], vertices[2], vertices[3])
        val bottom = arrayOf(vertices[4], vertices[5], vertices[6], vertices[7])
        val north = arrayOf(vertices[5], vertices[2], vertices[1], vertices[6])
        val east = arrayOf(vertices[6], vertices[1], vertices[0], vertices[7])
        val south = arrayOf(vertices[7], vertices[0], vertices[3], vertices[4])
        val west = arrayOf(vertices[4], vertices[3], vertices[2], vertices[5])

        val tessellator = Tessellator.getInstance()
        val renderer = tessellator.buffer

        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR)
        renderTexturedQuad(renderer, topTexture, top, 1.0, color.r, color.g, color.b, color.a)
        renderTexturedQuad(renderer, bottomTexture, bottom, 1.0, color.r, color.g, color.b, color.a)
        renderTexturedQuad(renderer, sideTexture, north, height, color.r, color.g, color.b, color.a)
        renderTexturedQuad(renderer, sideTexture, east, height, color.r, color.g, color.b, color.a)
        renderTexturedQuad(renderer, sideTexture, south, height, color.r, color.g, color.b, color.a)
        renderTexturedQuad(renderer, sideTexture, west, height, color.r, color.g, color.b, color.a)
        tessellator.draw()
    }

    private fun renderTexturedQuad(renderer: BufferBuilder, texture: TextureAtlasSprite, vertices: Array<Vec3d>, contentHeight: Double, r: Float, g: Float, b: Float, a: Float) {
        val minU = texture.minU.toDouble()
        val maxU = texture.maxU.toDouble()
        val minV = texture.getInterpolatedV(texture.height - texture.height * contentHeight).toDouble()
        val maxV = texture.maxV.toDouble()

        renderer.pos(vertices[0].x, vertices[0].y, vertices[0].z).tex(maxU, maxV).color(r, g, b, a).endVertex()
        renderer.pos(vertices[1].x, vertices[1].y, vertices[1].z).tex(maxU, minV).color(r, g, b, a).endVertex()
        renderer.pos(vertices[2].x, vertices[2].y, vertices[2].z).tex(minU, minV).color(r, g, b, a).endVertex()
        renderer.pos(vertices[3].x, vertices[3].y, vertices[3].z).tex(minU, maxV).color(r, g, b, a).endVertex()
    }
}
