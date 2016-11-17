package exnihiloomnia.client.textures.files;

import exnihiloomnia.ENO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TextureLoader {

	public static BufferedImage load(ResourceLocation location) {
		try {
			IResource res = Minecraft.getMinecraft().getResourceManager().getResource(location);
			return ImageIO.read(res.getInputStream());
		}
		catch (Exception e) {
			ENO.log.error("Failed to load image: " + location.toString());
			return null;
		}
	}
}
