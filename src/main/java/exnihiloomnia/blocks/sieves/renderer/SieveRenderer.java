package exnihiloomnia.blocks.sieves.renderer;

import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.helpers.ContentRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class SieveRenderer extends TileEntitySpecialRenderer<TileEntitySieve> {
	public static final double MIN_RENDER_CAPACITY = (double) 18/32;
	public static final double MAX_RENDER_CAPACITY = (double) 31/32;
	private static EntityLivingBase entity;
	
	@Override
	public void renderTileEntityAt(TileEntitySieve sieve, double x, double y, double z, float f, int i) {
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.translate(x + 0.025d, y, z + 0.025d);
		GlStateManager.scale(0.95d, 1.0d, 0.95d);

		renderMeshTexture(sieve);
		renderContents(sieve);

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	
	private void renderMeshTexture(TileEntitySieve sieve) {
		TextureAtlasSprite mesh = sieve.getMeshTexture();
		
		if (mesh != null) {
			GlStateManager.disableCull();
			ContentRenderHelper.renderContentsSimple(mesh, MIN_RENDER_CAPACITY, Color.WHITE);
			GlStateManager.enableCull();
		}
	}
	
	private void renderContents(TileEntitySieve sieve) {
		ItemStack contents = sieve.getContents();
		
		if (contents != null && Block.getBlockFromItem(contents.getItem()) != null) {
			if (entity == null) {
				entity = new EntityCreeper(sieve.getWorld());
			}

			double top = ContentRenderHelper.getAdjustedContentLevel(MIN_RENDER_CAPACITY, MAX_RENDER_CAPACITY, 1.0d - (double)sieve.getProgress());
			double height = top - MIN_RENDER_CAPACITY;

			GlStateManager.translate(0.5d, 0.5d, 0.5d); //Get the block place correctly.
			GlStateManager.translate(0.0d, MIN_RENDER_CAPACITY + 0.01d - ((1.0d - height) * 0.5d), 0.0d); //Lift the block into the sifting box.
			GlStateManager.scale(0.94d, height, 0.94d); //Adjust the height to fit the progress.

			Minecraft.getMinecraft().getItemRenderer().renderItem(entity, contents, TransformType.NONE);
		}
	}
}
