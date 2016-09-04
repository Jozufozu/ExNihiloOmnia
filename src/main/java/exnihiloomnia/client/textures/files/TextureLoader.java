package exnihiloomnia.client.textures.files;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import exnihiloomnia.ENO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class TextureLoader {

	public static BufferedImage load(ResourceLocation location)
	{
		try
		{
			IResource res = Minecraft.getMinecraft().getResourceManager().getResource(location);
			BufferedImage imgOutput = ImageIO.read(res.getInputStream());

			return imgOutput;
		}
		catch (Exception e)
		{
			ENO.log.error("Failed to load image: " + location.toString());
			return null;
		}
	}
}
