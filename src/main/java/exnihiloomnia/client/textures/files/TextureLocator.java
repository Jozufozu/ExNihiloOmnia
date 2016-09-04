package exnihiloomnia.client.textures.files;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class TextureLocator
{
  public static TextureAtlasSprite find(ResourceLocation loc)
  {
    return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(loc.toString());
  }
}
