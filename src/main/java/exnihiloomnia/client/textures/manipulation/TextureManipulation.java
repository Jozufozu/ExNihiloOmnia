package exnihiloomnia.client.textures.manipulation;

import com.google.common.math.IntMath;
import exnihiloomnia.ENO;
import exnihiloomnia.util.Color;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Random;

public class TextureManipulation {

    /**
     * Jumbles around the color data of an image
     * @param image the image to jumble
     * @return Chromatically aberrated image
     */
    public static BufferedImage aberrate(BufferedImage image, Random random) {
        int     w = image.getWidth(),
                h = image.getHeight();

        BufferedImage out = new BufferedImage(w, h, image.getType());

        byte[] inData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int[] outData = new int[w * h];

        //individual color arrays
        float[] rData = new float[w * h],
                gData = new float[w * h],
                bData = new float[w * h];

        //random offset
        int rOff = random.nextInt(7) - 3,
            gOff = random.nextInt(7) - 3,
            bOff = random.nextInt(7) - 3;

        //separate the channels
        for (int i = 0; i < inData.length; i++) {
            Color color = new Color(inData[i]);

            int r = i + rOff,
                g = i + gOff,
                b = i + bOff;

            r = IntMath.mod(r, rData.length);
            g = IntMath.mod(g, rData.length);
            b = IntMath.mod(b, rData.length);

            rData[r] = color.r;
            gData[g] = color.g;
            bData[b] = color.b;
        }

        for (int i = 0; i < outData.length; i++) {
            outData[i] = new Color(rData[i], gData[i], bData[i], new Color(inData[i]).a).toInt();
        }

        out.setRGB(0, 0, w, h, outData, 0, w);

        return out;
    }

    //Recolors an image based on the input color
    public static BufferedImage recolor(BufferedImage template, Color colorNew) {
        int w = template.getWidth();
        int h = template.getHeight();

        // create an OUTPUT image that we will use to override
        BufferedImage imgOutput = new BufferedImage(w, h, template.getType());

        int[] templateData = new int[w * h];
        int[] outputData = new int[w * h];

        // read the rgb color data into our array
        template.getRGB(0, 0, w, h, templateData, 0, w);

        // blend each pixel where alpha > 0
        for (int i = 0; i < templateData.length; i++) {
            Color colorRaw = new Color(templateData[i]);

            if (colorRaw.a > 0) {
                float a = colorRaw.a;
                float r = (colorNew.r);
                float g = (colorNew.g);
                float b = (colorNew.b);

                //System.out.println("Blended pixel r:" + r + ", g:" +  g + ", b:" + b + ", a:" +  a);
                outputData[i] = (new Color(r, g, b, a).toInt());
            }
            else {
                //System.out.println("Raw pixel r:" + colorRaw.r + ", g:" +  colorRaw.g + ", b:" + colorRaw.b + ", a:" +  colorRaw.a);
                outputData[i] = templateData[i];
            }
        }

        // write the new image data to the OUTPUT image buffer
        imgOutput.setRGB(0, 0, w, h, outputData, 0, w);

        return imgOutput;
    }

    //Combines two images and returns the composite.
    public static BufferedImage composite(BufferedImage imgBackground, BufferedImage imgForeground) {
        if (!normalCompositePossible(imgBackground, imgForeground)) {
            ENO.log.error("Images with different sizes can't be composited.");
            return null;
        }

        int w = imgBackground.getWidth();
        int h = imgBackground.getHeight();

        // create an OUTPUT image that we will use to override
        BufferedImage imgOutput = new BufferedImage(w, h, imgBackground.getType());

        int[] backgroundData = new int[w * h];
        int[] foregroundData = new int[w * h];
        int[] outputData = new int[w * h];

        // read the rgb color data into our array
        imgBackground.getRGB(0, 0, w, h, backgroundData, 0, w);
        imgForeground.getRGB(0, 0, w, h, foregroundData, 0, w);

        // 'over' blend each pixel
        for (int i = 0; i < backgroundData.length; i++) {
            Color colorBackground = new Color(backgroundData[i]);
            Color colorForeground = new Color(foregroundData[i], true);

            //Debug code!
            //System.out.println("Background pixel r:" + colorBackground.r + ", g:" +  colorBackground.g + ", b:" + colorBackground.b + ", a:" +  colorBackground.a);
            //System.out.println("Foreground pixel r:" + colorForeground.r + ", g:" +  colorForeground.g + ", b:" + colorForeground.b + ", a:" +  colorForeground.a);

            outputData[i] = backgroundData[i];

            if (colorForeground.a > 0) {
                float alpha = colorForeground.a;

                float a = colorBackground.a;
                float r = (colorForeground.r * alpha) + (colorBackground.r) * (1.0f - alpha);
                float g = (colorForeground.g * alpha) + (colorBackground.g) * (1.0f - alpha);
                float b = (colorForeground.b * alpha) + (colorBackground.b) * (1.0f - alpha);

                //System.out.println("Blended pixel r:" + r + ", g:" +  g + ", b:" + b + ", a:" +  a);
                outputData[i] = (new Color(r, g, b, a).toInt());
            }
            else {
                outputData[i] = backgroundData[i];
            }
        }

        // write the new image data to the OUTPUT image buffer
        imgOutput.setRGB(0, 0, w, h, outputData, 0, w);

        return imgOutput;
    }

    private static boolean normalCompositePossible(BufferedImage imgBackground, BufferedImage imgForeground) {
        //if the size is identical, then compositing in normal mode is possible.
        return (imgBackground.getWidth() == imgForeground.getWidth()) && (imgBackground.getHeight() == imgForeground.getHeight());
    }

//  private static boolean animatedCompositePossible(BufferedImage imgBackground, BufferedImage imgForeground) {
//  //if the width is identical and the height of one image is a multiple of the height of the other, compositing in animation mode is possible.
//    return (imgBackground.getHeight() % imgForeground.getHeight() == 0 || imgForeground.getHeight() % imgBackground.getHeight() == 0);
//  }
}
