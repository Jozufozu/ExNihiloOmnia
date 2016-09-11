package exnihiloomnia.items.meshs;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.items.ENOItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMesh extends Item implements ISieveMesh {
	private String texture_location;
	
	public ItemMesh()
	{
		super();
		
		this.setCreativeTab(ENOItems.ENO_TAB);
		if (!ENOConfig.classic_sieve)
			this.setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	public void setMeshTexture(String texture_location)
	{
		this.texture_location = texture_location;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public TextureAtlasSprite getMeshTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture_location);
	}

	@Override
	public int getItemEnchantability() {
		return 30;
	}
}
