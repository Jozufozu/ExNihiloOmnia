package exnihiloomnia.blocks.barrels.states.compost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.enums.EnumMetadataBehavior;

public class BarrelStateMycelium extends BarrelStateCompostSpecial{

	public BarrelStateMycelium()
	{
		super();
		
		addIngredient(new ItemStack(Items.GHAST_TEAR), EnumMetadataBehavior.IGNORED);
	}
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.mycelium";
	}
	
	@Override
	protected void renderBlockTexture(TileEntityBarrel barrel)
	{
		double timer = barrel.getTimerStatus();

		if (timer > 0.0d)
		{
			TextureAtlasSprite top = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mycelium_top");
			TextureAtlasSprite side = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mycelium_side");
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
