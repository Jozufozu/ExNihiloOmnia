package exnihiloomnia.blocks.barrels.states.compost;

import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BarrelStateCoarseDirt extends BarrelStateCompostSpecial {
	
	public BarrelStateCoarseDirt() {
		super();
		
		addIngredient(new ItemStack(Items.ROTTEN_FLESH, 1), EnumMetadataBehavior.IGNORED);
	}

	@Override
	public String getUniqueIdentifier() {
		return "barrel.coarse_dirt";
	}
	
	@Override
	protected void renderBlockTexture(TileEntityBarrel barrel) {
		double timer = barrel.getTimerStatus();

		if (timer > 0.0d) {
			TextureAtlasSprite dirt = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/coarse_dirt");
			
			if (barrel.getBlockType().getBlockState().getBaseState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(dirt, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);
			}
			else {
				BarrelRenderer.renderContentsComplex(dirt, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);
			}
		}
	}
}
