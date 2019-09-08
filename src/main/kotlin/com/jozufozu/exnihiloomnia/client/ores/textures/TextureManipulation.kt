package com.jozufozu.exnihiloomnia.client.ores.textures

import com.google.common.math.IntMath
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.util.*

object TextureManipulation {

    /**
     * Jumbles around the color data of an image
     *
     * @param image the image to jumble
     * @return Chromatically aberrated image
     */
    fun aberrate(image: BufferedImage, random: Random): BufferedImage {
        val w = image.width
        val h = image.height

        val out = BufferedImage(w, h, image.type)

        val inData = (image.raster.dataBuffer as DataBufferByte).data
        val outData = IntArray(w * h)

        // individual color arrays
        val rData = FloatArray(w * h)
        val gData = FloatArray(w * h)
        val bData = FloatArray(w * h)

        // random offset
        val rOff = random.nextInt(7) - 3
        val gOff = random.nextInt(7) - 3
        val bOff = random.nextInt(7) - 3

        // separate the channels
        for (i in inData.indices) {
            val color = Color(inData[i].toInt())

            var r = i + rOff
            var g = i + gOff
            var b = i + bOff

            r = IntMath.mod(r, rData.size)
            g = IntMath.mod(g, rData.size)
            b = IntMath.mod(b, rData.size)

            rData[r] = color.r
            gData[g] = color.g
            bData[b] = color.b
        }

        for (i in outData.indices) {
            outData[i] = Color(rData[i], gData[i], bData[i], Color(inData[i].toInt()).a).toInt()
        }

        out.setRGB(0, 0, w, h, outData, 0, w)

        return out
    }

    // Recolors an image based on the input color
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
                val a = colorRaw.a
                val r = colorNew.r
                val g = colorNew.g
                val b = colorNew.b

                outputData[i] = Color(r, g, b, a).toInt()
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

    //  private static boolean animatedCompositePossible(BufferedImage imgBackground, BufferedImage imgForeground) {
    //  //if the width is identical and the height of one image is a multiple of the height of the other, compositing in animation mode is possible.
    //    return (imgBackground.getHeight() % imgForeground.getHeight() == 0 || imgForeground.getHeight() % imgBackground.getHeight() == 0);
    //  }
}
