package com.jozufozu.exnihiloomnia.client.ores.textures

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ores.BlockType
import com.jozufozu.exnihiloomnia.common.ores.ItemType
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

@SideOnly(Side.CLIENT)
object ExNihiloOreTextures {
    fun register(map: TextureMap) {
        for (type in BlockType.values()) {
//            map.registerSprite(ResourceLocation(ExNihilo.MODID, "blocks/ore_${type}_template"))
//            map.registerSprite(ResourceLocation(ExNihilo.MODID, "blocks/ore_${type}_base"))

            val fore = load(ResourceLocation(ExNihilo.MODID, "textures/blocks/ore_${type}_template.png")) ?: continue
            val back = load(ResourceLocation(ExNihilo.MODID, "textures/blocks/ore_${type}_base.png")) ?: continue

            for (ore in RegistryManager.ORES) {
                if (ore.hasBlock(type)) {
                    val recolor = TextureManipulation.recolor(fore, ore.color)
                    val comp = TextureManipulation.composite(back, recolor) ?: continue

                    val sprite = GeneratedSprite.fromImage(ore.getNameForBlock(type), comp)
                    map.setTextureEntry(sprite)
                }
            }
        }

        for (type in ItemType.values()) {
//            map.registerSprite(ResourceLocation(ExNihilo.MODID, "items/ore_${type}_template"))
//            map.registerSprite(ResourceLocation(ExNihilo.MODID, "items/ore_${type}_base"))

            val fore = load(ResourceLocation(ExNihilo.MODID, "textures/items/ore_${type}_template.png")) ?: continue
            val back = load(ResourceLocation(ExNihilo.MODID, "textures/items/ore_${type}_base.png")) ?: continue

            for (ore in RegistryManager.ORES) {
                if (ore.hasItem(type)) {
                    val recolor = TextureManipulation.recolor(fore, ore.color)
                    val comp = TextureManipulation.composite(back, recolor) ?: continue

                    val sprite = GeneratedSprite.fromImage(ore.getNameForItem(type), comp)
                    map.setTextureEntry(sprite)
                }
            }
        }
    }

    private fun load(location: ResourceLocation): BufferedImage? {
        return try {
            val res = Minecraft.getMinecraft().resourceManager.getResource(location)
            ImageIO.read(res.inputStream)
        } catch (e: Exception) {
            ExNihilo.log.error("Failed to load image: $location")
            null
        }
    }
}
