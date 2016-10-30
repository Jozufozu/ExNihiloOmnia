package exnihiloomnia.blocks.barrels.states.slime;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.client.textures.files.TextureLocator;
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
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BarrelStateSlime extends BarrelState {
	private Color white = new Color("FFFFFF");
	private Color slime = new Color("33ff22");
	private static String[] description = new String[]{""};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.slime.green";
	}
	
	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null && fluid.getFluid() != null) {
			GlStateManager.pushMatrix();
			RenderHelper.disableStandardItemLighting();
			
			GlStateManager.disableBlend();

			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			TextureAtlasSprite water = TextureLocator.find(fluid.getFluid().getStill());

			GlStateManager.translate(x + 0.125d, y, z + 0.125d);
			GlStateManager.scale(0.75d, 1.0d, 0.75d);
			
			if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(water, 1.0d, Color.average(white, slime, (float)barrel.getTimerStatus()));
			}
			else {
				BarrelRenderer.renderContentsComplex(water, 1.0d, Color.average(white, slime, (float)barrel.getTimerStatus()));
			}
			
			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() >= 0 && barrel.getTimerStatus() < 1.0d ) {
			description[0] = I18n.format("exnihiloomnia.info.barrel.slime.summoning") + " " + I18n.format("entity.Slime.name") + " " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
		else if (barrel.getTimerStatus() >= 1.0d) {
			description[0] = I18n.format("exnihiloomnia.info.barrel.slime.summoned");
			return description;
		}
		
		return null;
	}

	@Override
	public void provideInformation(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

		if (barrel.getTimerStatus() >= 0 && barrel.getTimerStatus() < 1.0d ) {
			info.text(I18n.format("exnihiloomnia.info.barrel.slime.summoning") + " " + I18n.format("entity.Slime.name"));
			info.progress((int) (barrel.getTimerStatus() * 100), 100, info.defaultProgressStyle().filledColor(0xff339922).alternateFilledColor(0xff339922));
		}
		else {
			info.text(I18n.format("exnihiloomnia.info.barrel.slime.summoned"));
			info.entity("Slime", info.defaultEntityStyle().scale(4));
		}
	}
}
