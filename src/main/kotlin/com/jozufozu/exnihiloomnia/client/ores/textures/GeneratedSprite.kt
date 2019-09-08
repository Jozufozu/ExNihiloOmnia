package com.jozufozu.exnihiloomnia.client.ores.textures

import com.google.common.collect.Lists
import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ResourceLocation

import java.awt.image.BufferedImage
import java.util.function.Function

class GeneratedSprite(spriteName: String) : TextureAtlasSprite(spriteName) {

    fun loadImage(image: BufferedImage) {
        resetSprite()

        width = image.width
        height = image.height

        if (height != width) {
            ExNihilo.log.error("Width and height should be equal, got ${width}x${height}")
            return
        }

        val mipmaps = arrayOfNulls<IntArray>(Minecraft.getMinecraft().gameSettings.mipmapLevels + 1)

        mipmaps[0] = IntArray(width * height)
        image.getRGB(0, 0, width, height, mipmaps[0], 0, width)

        framesTextureData.add(mipmaps)
    }

    private fun resetSprite() {
        this.setFramesTextureData(Lists.newArrayList())
        this.frameCounter = 0
        this.tickCounter = 0
    }

    override fun hasCustomLoader(manager: IResourceManager?, location: ResourceLocation?): Boolean {
        return true
    }

    override fun load(manager: IResourceManager?, location: ResourceLocation?, textureGetter: Function<ResourceLocation, TextureAtlasSprite>?): Boolean {
        return false
    }

    companion object {

        fun fromImage(name: ResourceLocation, image: BufferedImage): GeneratedSprite {
            val sprite = GeneratedSprite(name.toString())
            sprite.loadImage(image)

            return sprite
        }
    }
}
