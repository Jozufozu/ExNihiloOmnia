package exnihiloomnia.client.textures.files;

import net.minecraft.util.ResourceLocation;

public class TextureLocations {
	
	public static ResourceLocation getBlockLocation(String source, String name) {
		int ind = name.indexOf(58);

		if (ind >= 0) {
			if (ind > 1) {
				source = name.substring(0, ind);
			}

			name = name.substring(ind + 1, name.length());
		}

		source = source.toLowerCase();
		name = "textures/blocks/" + name.toLowerCase() + ".png";

		return new ResourceLocation(source, name);
	}

	public static ResourceLocation getItemLocation(String source, String name) {
		int ind = name.indexOf(58);

		if (ind >= 0) {
			if (ind > 1) {
				source = name.substring(0, ind);
			}

			name = name.substring(ind + 1, name.length());
		}

		source = source.toLowerCase();
		name = "textures/items/" + name.toLowerCase() + ".png";

		return new ResourceLocation(source, name);
	}
}
