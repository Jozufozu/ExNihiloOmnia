package com.jozufozu.exnihiloomnia.client;

import com.jozufozu.exnihiloomnia.common.util.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtil
{
    /**
     * Draws a flat texture on the xz plane from (0,0) to (1,1). Be sure to translate and scale to your needs
     */
    public static void renderContents(TextureAtlasSprite texture, double height, Color color)
    {
        double minU = (double) texture.getMinU();
        double maxU = (double) texture.getMaxU();
        double minV = (double) texture.getMinV();
        double maxV = (double) texture.getMaxV();
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.pos(1, height, 1).tex(maxU, maxV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(1, height, 0).tex(maxU, minV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(0, height, 0).tex(minU, minV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(0, height, 1).tex(minU, maxV).color(color.r, color.g, color.b, color.a).endVertex();
        tessellator.draw();
    }
    
    public static void renderContents3D(TextureAtlasSprite topTexture, TextureAtlasSprite sideTexture, double height, Color color)
    {
        renderContents3D(topTexture, sideTexture, topTexture, height, color);
    }
    
    public static void renderContents3D(TextureAtlasSprite topTexture, TextureAtlasSprite sideTexture, TextureAtlasSprite bottomTexture, double height, Color color)
    {
        Vec3d[] vertices = {new Vec3d(1, height, 1), new Vec3d(1, height, 0), new Vec3d(0, height, 0), new Vec3d(0, height, 1), new Vec3d(0, 0, 1), new Vec3d(0, 0, 0), new Vec3d(1, 0, 0), new Vec3d(1, 0, 1)};
        Vec3d[] top = {vertices[0], vertices[1], vertices[2], vertices[3]};
        Vec3d[] bottom = {vertices[4], vertices[5], vertices[6], vertices[7]};
        Vec3d[] north = {vertices[5], vertices[2], vertices[1], vertices[6]};
        Vec3d[] east = {vertices[6], vertices[1], vertices[0], vertices[7]};
        Vec3d[] south = {vertices[7], vertices[0], vertices[3], vertices[4]};
        Vec3d[] west = {vertices[4], vertices[3], vertices[2], vertices[5]};
    
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
    
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        renderTexturedQuad(renderer, topTexture, top, 1, color.r, color.g, color.b, color.a);
        renderTexturedQuad(renderer, bottomTexture, bottom, 1, color.r, color.g, color.b, color.a);
        renderTexturedQuad(renderer, sideTexture, north, height, color.r, color.g, color.b, color.a);
        renderTexturedQuad(renderer, sideTexture, east, height, color.r, color.g, color.b, color.a);
        renderTexturedQuad(renderer, sideTexture, south, height, color.r, color.g, color.b, color.a);
        renderTexturedQuad(renderer, sideTexture, west, height, color.r, color.g, color.b, color.a);
        tessellator.draw();
    }
    
    private static void renderTexturedQuad(BufferBuilder renderer, TextureAtlasSprite texture, Vec3d[] vertices, double contentHeight, float r, float g, float b, float a)
    {
        double minU = (double) texture.getMinU();
        double maxU = (double) texture.getMaxU();
        double minV = (double) texture.getInterpolatedV(texture.getIconHeight() - (texture.getIconHeight() * contentHeight));
        double maxV = (double) texture.getMaxV();
        
        renderer.pos(vertices[0].x, vertices[0].y, vertices[0].z).tex(maxU, maxV).color(r, g, b, a).endVertex();
        renderer.pos(vertices[1].x, vertices[1].y, vertices[1].z).tex(maxU, minV).color(r, g, b, a).endVertex();
        renderer.pos(vertices[2].x, vertices[2].y, vertices[2].z).tex(minU, minV).color(r, g, b, a).endVertex();
        renderer.pos(vertices[3].x, vertices[3].y, vertices[3].z).tex(minU, maxV).color(r, g, b, a).endVertex();
    }
}
