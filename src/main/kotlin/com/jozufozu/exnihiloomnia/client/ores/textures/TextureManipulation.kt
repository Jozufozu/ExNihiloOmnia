package com.jozufozu.exnihiloomnia.client.ores.textures

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.Color
import java.awt.image.BufferedImage

object TextureManipulation {
    // Sets all of the non fully transparent pixels to be the given color
    fun recolor(template: BufferedImage, colorNew: Color): BufferedImage {
        val w = template.width
        val h = template.height

        // create an OUTPUT image that we will use to override
        val imgOutput = BufferedImage(w, h, template.type)

        val templateData = IntArray(w * h)
        val outputData = IntArray(w * h)

        // read the rgb color data into our array
        template.getRGB(0, 0, w, h, templateData, 0, w)

        // blend each pixel where alpha > 0
        for (i in templateData.indices) {
            val colorRaw = Color(templateData[i], true)

            if (colorRaw.a > 0) {
                outputData[i] = Color.toInt(colorNew.r, colorNew.g, colorNew.b, colorRaw.a)
            } else {
                outputData[i] = templateData[i]
            }
        }

        // write the new image data to the OUTPUT image buffer
        imgOutput.setRGB(0, 0, w, h, outputData, 0, w)

        return imgOutput
    }

    //Combines two images and returns the composite.
    fun composite(background: BufferedImage, foreground: BufferedImage): BufferedImage? {
        if (!normalCompositePossible(background, foreground)) {
            ExNihilo.log.error("Images with different sizes can't be composited.")
            return null
        }

        val w = background.width
        val h = background.height

        // create an OUTPUT image that we will use to override
        val imgOutput = BufferedImage(w, h, background.type)

        val backgroundData = IntArray(w * h)
        val foregroundData = IntArray(w * h)
        val outputData = IntArray(w * h)

        // read the rgb color data into our array
        background.getRGB(0, 0, w, h, backgroundData, 0, w)
        foreground.getRGB(0, 0, w, h, foregroundData, 0, w)

        // 'over' blend each pixel
        for (i in backgroundData.indices) {
            val colorBackground = Color(backgroundData[i])
            val colorForeground = Color(foregroundData[i], true)

            outputData[i] = backgroundData[i]

            if (colorForeground.a > 0) {
                val alpha = colorForeground.a

                val a = colorBackground.a
                val r = colorForeground.r * alpha + colorBackground.r * (1.0f - alpha)
                val g = colorForeground.g * alpha + colorBackground.g * (1.0f - alpha)
                val b = colorForeground.b * alpha + colorBackground.b * (1.0f - alpha)

                outputData[i] = Color(r, g, b, a).toInt()
            } else {
                outputData[i] = backgroundData[i]
            }
        }

        // write the new image data to the OUTPUT image buffer
        imgOutput.setRGB(0, 0, w, h, outputData, 0, w)

        return imgOutput
    }

    /**
     * Whether or not it is possible to composite two images
     */
    private fun normalCompositePossible(background: BufferedImage, foreground: BufferedImage): Boolean {
        return background.width == foreground.width && background.height == foreground.height
    }
}
