package exnihiloomnia.util.helpers;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import exnihiloomnia.util.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ContentRenderHelper {
	public static double getAdjustedContentLevel(double min, double max, double fullness) {
		double capacity = max - min;
		double adjusted = fullness * capacity;		
		adjusted += min;
		
		return adjusted;
	}
	
	public static void renderContentsSimple(TextureAtlasSprite texture, double height, Color color) {
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		
		double minU = (double)texture.getMinU();
		double maxU = (double)texture.getMaxU();
		double minV = (double)texture.getMinV();
		double maxV = (double)texture.getMaxV();
		
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		GlStateManager.color(color.r, color.g, color.b, color.a);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(1.0d, 	height, 	1.0d).tex(maxU, maxV).endVertex();
		renderer.pos(1.0d, 	height, 	0).tex(maxU, minV).endVertex();
		renderer.pos(0, 	height, 	0).tex(minU, minV).endVertex();
		renderer.pos(0, 	height, 	1.0d).tex(minU, maxV).endVertex();
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	public static void renderContentsFancy(TextureAtlasSprite texture, double height, Color color) {
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();

		double 	minU = (double)texture.getMinU(),
				maxU = (double)texture.getMaxU(),
				minV = (double)texture.getMinV(),
				maxV = (double)texture.getMaxV();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		GlStateManager.color(color.r, color.g, color.b, color.a);

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		double angle = ((Minecraft.getSystemTime() / 10) % 360) * 0.0174533;
		renderer.pos(1.0d, 	height + Math.sin(angle) * .005 * height,	1.0d).tex(maxU, maxV).endVertex();
		renderer.pos(1.0d, 	height + Math.sin(angle + 3.14159265 * .5) * .005 * height, 	0).tex(maxU, minV).endVertex();
		renderer.pos(0, 	height + Math.sin(angle + 3.14159265) * .005 * height, 	0).tex(minU, minV).endVertex();
		renderer.pos(0, 	height + Math.sin(angle + 3.14159265 * 1.5) * .005 * height, 	1.0d).tex(minU, maxV).endVertex();
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
