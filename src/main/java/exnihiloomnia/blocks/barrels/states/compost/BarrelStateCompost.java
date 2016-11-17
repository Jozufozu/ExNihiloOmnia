package exnihiloomnia.blocks.barrels.states.compost;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.Color;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

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
			description[0] = I18n.format("exnihiloomnia.info.barrel.compost.collecting") + " " + String.format("%.0f", barrel.getVolumeProportion() * 100) + "%";
			return description;
		}
		else {
			description[0] = I18n.format("exnihiloomnia.info.barrel.compost.composting") + " " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
	}

	@Override
	public void provideInformation(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		int color = barrel.getColor().toInt();

		if (barrel.getTimerStatus() == -1.0d) {
			info.text("Collecting Compost Materials");
			info.progress(barrel.getVolume(), barrel.getVolumeMax(), info.defaultProgressStyle().filledColor(color).alternateFilledColor(color).showText(false));
		}
		else {
			info.text("Composting");
			info.progress(100 - (int)(barrel.getTimerStatus() * 100), 100, info.defaultProgressStyle().filledColor(color).alternateFilledColor(color).showText(false));
		}
	}
}
