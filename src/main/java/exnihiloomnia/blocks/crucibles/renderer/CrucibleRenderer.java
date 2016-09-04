package exnihiloomnia.blocks.crucibles.renderer;

import org.lwjgl.opengl.GL11;

import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.client.textures.files.TextureLocator;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.helpers.ContentRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CrucibleRenderer extends TileEntitySpecialRenderer<TileEntityCrucible>{
	public static final double MIN_RENDER_CAPACITY = 0.30d;
	public static final double MAX_RENDER_CAPACITY = 0.95d;
	public static EntityLivingBase entity;
	
	@Override
	public void renderTileEntityAt(TileEntityCrucible crucible, double x, double y, double z, float partialTicks, int destroyStage)
	{

		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.translate(x + 0.025d, y, z + 0.025d);
		GlStateManager.scale(0.95d, 1.0d, 0.95d);

		if (crucible.getSolidFullness() >= crucible.getFluidFullness())
		{
			renderSolidContents(crucible);
		}
		else
		{
			renderFluidContents(crucible);
		}


		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	
	private void renderSolidContents(TileEntityCrucible crucible)
	{
		ItemStack contents = crucible.getLastItemAdded();
		
		if (contents != null && Block.getBlockFromItem(contents.getItem()) != null)
		{
		  if (entity == null)
		  {
		    entity = new EntityCreeper(crucible.getWorld());
		  }
		  
			double top = ContentRenderHelper.getAdjustedContentLevel(MIN_RENDER_CAPACITY, MAX_RENDER_CAPACITY, crucible.getSolidFullness());
			double height = top - MIN_RENDER_CAPACITY;
			
			GlStateManager.translate(0.5d, 0.5d, 0.5d); //Get the block place correctly.
			GlStateManager.translate(0.0d, MIN_RENDER_CAPACITY + 0.01d - ((1.0d - height) * 0.5d), 0.0d); //Lift the block into the sifting box.
			GlStateManager.scale(0.94d, height, 0.94d); //Adjust the height to fit the progress.
			
			Minecraft.getMinecraft().getItemRenderer().renderItem(entity, contents, TransformType.NONE);
		}
	}
	
	private void renderFluidContents(TileEntityCrucible crucible)
	{
		FluidStack contents = crucible.getCurrentFluid();
		
		if (contents != null && contents.getFluid() != null)
		{
			double height = ContentRenderHelper.getAdjustedContentLevel(MIN_RENDER_CAPACITY, MAX_RENDER_CAPACITY, crucible.getFluidFullness());
			
			ContentRenderHelper.renderContentsSimple(TextureLocator.find(contents.getFluid().getStill()), height, Color.WHITE);
		}
	}
}
