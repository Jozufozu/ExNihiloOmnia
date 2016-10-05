package exnihiloomnia.client.textures;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.client.textures.files.DynamicTextureSprite;
import exnihiloomnia.client.textures.files.TextureLoader;
import exnihiloomnia.client.textures.files.TextureLocations;
import exnihiloomnia.client.textures.manipulation.TextureManipulation;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.meshs.ItemMesh;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumOre;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

public class ENOTextures {


	public static void registerCustomTextures(TextureMap map) {
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/compost"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/sieve_mesh_silk_white"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/sieve_mesh_wood"));

		registerFluidTextures(map);
	}

	public static void registerOreTextures(TextureMap map) {
        Random random = new Random();
        for (EnumOreBlockType type : EnumOreBlockType.values()) {
            String templateName = type.getName().substring(0, type.getName().length() < 6 ? type.getName().length(): 6);

            map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/" + templateName + "_template"));
            map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/" + type.getName() + "_base"));

            BufferedImage fore = TextureLoader.load(TextureLocations.getBlockLocation("exnihiloomnia", templateName + "_template"));
            BufferedImage back = TextureLoader.load(TextureLocations.getBlockLocation("exnihiloomnia", type.getName() + "_base"));

            for (EnumOre ore : EnumOre.values()) {
                BufferedImage recolor = TextureManipulation.recolor(fore, new Color(ore.getColor()));
                BufferedImage comp;
                if (ENOConfig.glitch_art)
                    comp = TextureManipulation.composite(TextureManipulation.aberrate(back, random), recolor);
                else
                    comp = TextureManipulation.composite(back, recolor);

                DynamicTextureSprite sprite = DynamicTextureSprite.fromImage(new ResourceLocation("exnihiloomnia:ore_" + type.getName() + "_" + ore.getName()), comp);
                forceTextureRegistration(map, sprite);
            }
		}
	}
	
	public static void setMeshTextures() {
		((ItemMesh) ENOItems.MESH_SILK_WHITE).setMeshTexture("exnihiloomnia:blocks/sieve_mesh_silk_white");
		((ItemMesh) ENOItems.MESH_WOOD).setMeshTexture("exnihiloomnia:blocks/sieve_mesh_wood");
	}
	
	private static void registerFluidTextures(TextureMap map) {
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/witchwater_still"));
		map.registerSprite(new ResourceLocation("exnihiloomnia:blocks/witchwater_flowing"));
	}

    private static void forceTextureRegistration(TextureMap map, TextureAtlasSprite sprite) {

        if (!map.setTextureEntry(sprite.getIconName(), sprite)) {
            ENO.log.debug("Failed to register texture: " + sprite.getIconName());
            try {
                Field f;
                f = map.getClass().getDeclaredField("mapRegisteredSprites");
                f.setAccessible(true);

                Map mapRegisteredSprites = (Map) f.get(map);

                mapRegisteredSprites.put(sprite.getIconName(), sprite);
            } catch (Exception e1) {
                ENO.log.debug("Failed to forcibly register texture: " + sprite.getIconName());

                try {
                    Field f;
                    f = map.getClass().getDeclaredField("field_110574_e");
                    f.setAccessible(true);

                    Map mapRegisteredSprites = (Map) f.get(map);

                    mapRegisteredSprites.put(sprite.getIconName(), sprite);
                } catch (Exception e2) {
                    ENO.log.error("Failed to forcibly register texture: " + sprite.getIconName());
                }
            }
        }
    }
}
