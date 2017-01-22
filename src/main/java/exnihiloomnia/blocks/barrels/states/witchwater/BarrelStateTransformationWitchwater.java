package exnihiloomnia.blocks.barrels.states.witchwater;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.client.textures.files.TextureLocator;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.util.Color;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

public class BarrelStateTransformationWitchwater extends BarrelState {
	private static final String[] description = new String[]{""};

	@Override
	public String getUniqueIdentifier() {
		return "barrel.transformation.witchwater";
	}

	@Override
	public boolean canManipulateFluids(TileEntityBarrel barrel) {
		return false;
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
			GlStateManager.pushMatrix();
			RenderHelper.disableStandardItemLighting();

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.translate(x + 0.125d, y, z + 0.125d);
			GlStateManager.scale(0.75d, 1.0d, 0.75d);

			FluidTank tank = barrel.getFluidTank();
			if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(TextureLocator.find(FluidRegistry.WATER.getStill()), (double)tank.getFluidAmount() / (double)tank.getCapacity(), new Color(1.0f, 1.0f, 1.0f, 1.0f - (float)barrel.getTimerStatus()));
				BarrelRenderer.renderContentsSimple(TextureLocator.find(ENOFluids.WITCHWATER.getStill()), (double)tank.getFluidAmount() / (double)tank.getCapacity(), new Color(1.0f, 1.0f, 1.0f, (float)barrel.getTimerStatus()));
			}
			else {
				BarrelRenderer.renderContentsComplex(TextureLocator.find(FluidRegistry.WATER.getStill()), (double)tank.getFluidAmount() / (double)tank.getCapacity(), new Color(1.0f, 1.0f, 1.0f, 1.0f - (float)barrel.getTimerStatus()));
				BarrelRenderer.renderContentsComplex(TextureLocator.find(ENOFluids.WITCHWATER.getStill()), (double)tank.getFluidAmount() / (double)tank.getCapacity(), new Color(1.0f, 1.0f, 1.0f, (float)barrel.getTimerStatus()));
			}

			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() > 0) {
			description[0] = I18n.format("exnihiloomnia.info.barrel.witchwater") + " " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
		else {
			return null;
		}
	}

	@Override
	public void provideInformation(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		info.text("Fermenting");
		info.progress((int)(barrel.getTimerStatus() * 100), 100, info.defaultProgressStyle().filledColor(0xff5305cf).alternateFilledColor(0xff000000));
	}
}
