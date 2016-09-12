package exnihiloomnia.blocks.barrels.states.compost;

import org.lwjgl.opengl.GL11;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

public class BarrelStateCompost extends BarrelState {
	protected static Color white = new Color("FFFFFF");
	private static String[] description = new String[]{""};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.compost";
	}
	
	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.translate(x + 0.125d, y, z + 0.125d);
		GlStateManager.scale(0.75d, 1.0d, 0.75d);
		
		renderBlockTexture(barrel);
		renderCompostTexture(barrel);

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	
	protected void renderBlockTexture(TileEntityBarrel barrel) {
		double timer = barrel.getTimerStatus();

		if (timer > 0.0d) {
			TextureAtlasSprite dirt = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
			
			if (barrel.getBlockType().getBlockState().getBaseState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(dirt, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);
			}
			else {
				BarrelRenderer.renderContentsComplex(dirt, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);
			}
		}
	}
	
	protected void renderCompostTexture(TileEntityBarrel barrel) {
		double timer = barrel.getTimerStatus();
		
		if (timer < 1.0d) {
			TextureAtlasSprite compost = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exnihiloomnia:blocks/compost");
			
			Color colorA = barrel.getColor();
			Color colorB = new Color(colorA.r, colorA.g, colorA.b, colorA.a * (1.0f - (float)timer));
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			if (barrel.getBlockType().getBlockState().getBaseState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(compost, barrel.getVolumeProportion(), colorB);
			}
			else {
				BarrelRenderer.renderContentsComplex(compost, barrel.getVolumeProportion(), colorB);
			}
		}
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() == -1.0d) {
			description[0] = "Collecting Compost Materials " + String.format("%.0f", barrel.getVolumeProportion() * 100) + "%";
			return description;
		}
		else {
			description[0] = "Composting " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
	}
}
