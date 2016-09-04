package exnihiloomnia.blocks.barrels.states.compost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;

public class BarrelStatePodzol extends BarrelStateCompost{
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.podzol";
	}
	
	@Override
	protected void renderBlockTexture(TileEntityBarrel barrel)
	{
		double timer = barrel.getTimerStatus();

		if (timer > 0.0d)
		{
			TextureAtlasSprite top = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt_podzol_top");
			TextureAtlasSprite side = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt_podzol_side");
			TextureAtlasSprite bottom = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
			
			if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque())
			{
				BarrelRenderer.renderContentsSimple(top, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);
			}
			else
			{
				BarrelRenderer.renderContentsMultiTexture(top, side, bottom, (double)barrel.getVolume() / (double)barrel.getVolumeMax(), white);

			}
		}
	}
}
