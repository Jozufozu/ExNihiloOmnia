package exnihiloomnia.blocks.barrels.states.fluid;

import exnihiloomnia.util.Color;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class BarrelStateFluid extends BarrelState {
	private static String[] description = new String[]{""};

	@Override
	public String getUniqueIdentifier() {
		return "barrel.fluid";
	}

	@Override
	public boolean canManipulateFluids(TileEntityBarrel barrel) {
		return true;
	}
	
	@Override
	public int getLuminosity(TileEntityBarrel barrel) {
		FluidStack fluid = barrel.getFluid();
		
		if (fluid != null)
			return fluid.getFluid().getLuminosity();
		
		return 0;
	}

	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null && fluid.getFluid() != null) {
			ResourceLocation still = fluid.getFluid().getStill();
			
			if (still != null) {
				GlStateManager.pushMatrix();
				RenderHelper.disableStandardItemLighting();
				
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				Minecraft mc = Minecraft.getMinecraft();
				TextureManager textureManager = mc.getTextureManager();

				textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

				TextureAtlasSprite texture = mc.getTextureMapBlocks().getAtlasSprite(still.toString());

				GlStateManager.translate(x + 0.125d, y, z + 0.125d);
				GlStateManager.scale(0.75d, 1.0d, 0.75d);

				FluidTank tank = barrel.getFluidTank();

				if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque()) {
					BarrelRenderer.renderContentsSimple(texture, (double)tank.getFluidAmount() / (double)tank.getCapacity(), barrel.getColor());
				}
				else {
					BarrelRenderer.renderContentsComplex(texture, (double)tank.getFluidAmount() / (double)tank.getCapacity(), barrel.getColor());
				}
				
				RenderHelper.enableStandardItemLighting();
				GlStateManager.popMatrix();
			}
		}
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getFluid() != null) {
			description[0] = barrel.getFluid().getLocalizedName() + " " + barrel.getFluid().amount + "mB";
			return description;
		}
		else {
			return null;
		}
	}
}
