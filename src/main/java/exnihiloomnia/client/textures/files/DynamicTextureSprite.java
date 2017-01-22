package exnihiloomnia.client.textures.files;

import com.google.common.collect.Lists;
import exnihiloomnia.ENO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DynamicTextureSprite extends TextureAtlasSprite {

    public DynamicTextureSprite(String spriteName) {
        super(spriteName);
    }

    public void loadImage(BufferedImage image)
    {
        BufferedImage[] images = new BufferedImage[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1];
        images[0] = image;

        try
        {
            this.loadSprite(images);
        }
        catch (IOException e){
            ENO.log.error("Could not load BufferedImage: " + e);
        }
    }

    //old vanilla method
    public void loadSprite(BufferedImage[] images) throws IOException
    {
        this.resetSprite();
        int i = images[0].getWidth();
        int j = images[0].getHeight();
        this.width = i;
        this.height = j;
        int[][] aint = new int[images.length][];

        for (int k = 0; k < images.length; ++k) {
            BufferedImage bufferedimage = images[k];

            if (bufferedimage != null) {
                if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k)) {
                    throw new IOException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] {Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k)}));
                }

                aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
                bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
            }
        }

        if (j != i) {
            throw new RuntimeException("broken aspect ratio and not an animation");
        }

        this.framesTextureData.add(aint);
    }

    private void resetSprite()
    {
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public static DynamicTextureSprite fromImage(ResourceLocation loc, BufferedImage image)
    {
        DynamicTextureSprite sprite = new DynamicTextureSprite(loc.toString());
        sprite.loadImage(image);

        return sprite;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        return false;
    }
}
