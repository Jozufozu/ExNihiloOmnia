package exnihiloomnia.client.textures;

import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.meshs.ItemMesh;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class ENTextures {
	public static void registerCustomTextures(TextureMap map)
	{
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/compost"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/sieve_mesh_silk_white"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/sieve_mesh_wood"));
		
		registerFluidTextures(map);
	}
	
	public static void setMeshTextures()
	{
		((ItemMesh) ENOItems.MESH_SILK_WHITE).setMeshTexture("exnihiloomnia:blocks/sieve_mesh_silk_white");
		((ItemMesh) ENOItems.MESH_WOOD).setMeshTexture("exnihiloomnia:blocks/sieve_mesh_wood");
	}
	
	private static void registerFluidTextures(TextureMap map)
	{
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/witchwater_still"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/witchwater_flowing"));
	}
}
