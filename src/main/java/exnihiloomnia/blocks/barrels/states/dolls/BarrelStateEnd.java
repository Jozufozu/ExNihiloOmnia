package exnihiloomnia.blocks.barrels.states.dolls;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.client.textures.files.TextureLocator;
import exnihiloomnia.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.FluidStack;

public class BarrelStateEnd extends BarrelState{
	private Color white = new Color("FFFFFF");
	private Color black = new Color("000000");
	private static String[] description = new String[]{""};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.end";
	}
	
	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null && fluid.getFluid() != null)
		{
			GlStateManager.pushMatrix();
			RenderHelper.disableStandardItemLighting();
			
			GlStateManager.disableBlend();

			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			TextureAtlasSprite water = TextureLocator.find(fluid.getFluid().getStill());

			GlStateManager.translate(x + 0.125d, y, z + 0.125d);
			GlStateManager.scale(0.75d, 1.0d, 0.75d);
			
			if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque())
			{
				BarrelRenderer.renderContentsSimple(water, 1.0d, Color.average(white, black, (float)barrel.getTimerStatus()));
			}
			else
			{
				BarrelRenderer.renderContentsComplex(water, 1.0d, Color.average(white, black, (float)barrel.getTimerStatus()));
			}
			
			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel)
	{
		if (barrel.getTimerStatus() >= 0 && barrel.getTimerStatus() < 1.0d )
		{
			description[0] = "Relocating Enderman " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
		else if (barrel.getTimerStatus() >= 1.0d)
		{
			description[0] = "Enderman got!";
			return description;
		}
		
		return null;
	}
}
