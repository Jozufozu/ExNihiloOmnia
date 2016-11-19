package exnihiloomnia.blocks.barrels.states.output;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BarrelStateOutput extends BarrelState {
	private static String[] description = new String[]{""};
	private static EntityLivingBase entity;
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.simple";
	}
	
	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		ItemStack contents = barrel.getContents();

		if (contents != null) {
			if (entity == null) {
				//getItemRenderer.renderItem will not work unless a non-null entity is passed to it.
				//thats the only reason this exists. :(
			    entity = new EntityCreeper(barrel.getWorld());
			}
		  
			GlStateManager.pushMatrix();

			if (contents.getItem() instanceof ItemBlock) {
				GlStateManager.disableLighting();
				GlStateManager.translate(x + 0.5d, y + 0.5d, z + 0.5d);
				GlStateManager.scale(0.75d, 0.9d, 0.75d);
			}
			else {
				GlStateManager.disableLighting();
				GlStateManager.translate(x + 0.5d, y + 2d/16d, z + 0.5d);
				GlStateManager.rotate(-90, 1, 0, 0);
				GlStateManager.scale(0.75d, 0.75d, 0.75d);
			}
			
			Minecraft.getMinecraft().getItemRenderer().renderItem(entity, contents, TransformType.NONE);
			
			GlStateManager.enableLighting();

			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean canExtractContents(TileEntityBarrel barrel) {
		return true;
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getContents() != null) {
			description[0] = barrel.getContents().getDisplayName();
			return description;
		}
		else {
			return null;
		}
	}

	@Override
	public void provideInformation(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		if (barrel.getContents() != null) {
			info.text(barrel.getContents().getDisplayName());
			info.item(barrel.getContents());
		}
	}
}
