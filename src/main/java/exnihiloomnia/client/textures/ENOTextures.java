package exnihiloomnia.client.textures;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.client.textures.files.DynamicTextureSprite;
import exnihiloomnia.client.textures.files.TextureLoader;
import exnihiloomnia.client.textures.files.TextureLocations;
import exnihiloomnia.client.textures.manipulation.TextureManipulation;
import exnihiloomnia.compatibility.ENOOres;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.meshs.ItemMesh;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumOre;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class ENOTextures {


	public static void registerCustomTextures(TextureMap map) {
		map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/compost"));
		map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/sieve_mesh_silk_white"));
		map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/sieve_mesh_wood"));

		registerFluidTextures(map);
	}

	public static void registerOreTextures(TextureMap map) {
        Random random = new Random();
        for (EnumOreBlockType type : EnumOreBlockType.values()) {
            String templateName = type.getName().substring(0, type.getName().length() < 6 ? type.getName().length(): 6);

            map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/" + templateName + "_template"));
            map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/" + type.getName() + "_base"));

            BufferedImage fore = TextureLoader.load(TextureLocations.getBlockLocation(ENO.MODID, templateName + "_template"));
            BufferedImage back = TextureLoader.load(TextureLocations.getBlockLocation(ENO.MODID, type.getName() + "_base"));

            for (EnumOre ore : EnumOre.values()) {
                BufferedImage recolor = TextureManipulation.recolor(fore, new Color(ore.getColor()));
                BufferedImage comp;
                if (ENOConfig.glitch_art)
                    comp = TextureManipulation.composite(TextureManipulation.aberrate(back, random), recolor);
                else
                    comp = TextureManipulation.composite(back, recolor);

                DynamicTextureSprite sprite = DynamicTextureSprite.fromImage(ENOOres.getOreName(ore, type), comp);
                forceTextureRegistration(map, sprite);
            }
		}
	}
	
	public static void setMeshTextures() {
		((ItemMesh) ENOItems.MESH_SILK_WHITE).setMeshTexture(ENO.MODID + ":blocks/sieve_mesh_silk_white");
		((ItemMesh) ENOItems.MESH_WOOD).setMeshTexture(ENO.MODID + ":blocks/sieve_mesh_wood");
	}
	
	private static void registerFluidTextures(TextureMap map) {
		map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/witchwater_still"));
		map.registerSprite(new ResourceLocation(ENO.MODID + ":blocks/witchwater_flowing"));
	}

    @SuppressWarnings("unchecked")
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
