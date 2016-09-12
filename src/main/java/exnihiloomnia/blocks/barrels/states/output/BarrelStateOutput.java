package exnihiloomnia.blocks.barrels.states.output;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;

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
			
			GlStateManager.disableLighting();
			GlStateManager.translate(x + 0.5d, y + 0.5d, z + 0.5d);
			GlStateManager.scale(0.75d, 0.9d, 0.75d);
			
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
}
