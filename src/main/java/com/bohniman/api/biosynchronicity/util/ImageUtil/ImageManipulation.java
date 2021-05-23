package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageManipulation {

    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int w, int h) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = resizedImage.createGraphics();
        g.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g.drawImage(originalImage, 0, 0, w, h, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        return resizedImage;
    }

    public static BufferedImage getEnhancedBWImage(BufferedImage croppedImage) {
        for (int y = 0; y < croppedImage.getHeight(); y++) {

            for (int x = 0; x < croppedImage.getWidth(); x++) {
                // Retrieving contents of a pixel
                int pixel = croppedImage.getRGB(x, y);
                // Creating a Color object from pixel value

                // getting the color hue of the pixel RGB for comparision
                float hsb[] = new float[3];
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = (pixel) & 0xFF;

                hsb = Color.RGBtoHSB(r, g, b, hsb);

                float brightness = hsb[2] * 100;

                if (brightness >= 60 && brightness <= 80) {
                    Color color = new Color(0, 0, 0);
                    croppedImage.setRGB(x, y, color.getRGB());
                } else {
                    Color color = new Color(255, 255, 255);
                    croppedImage.setRGB(x, y, color.getRGB());
                }
            }

        }
        return croppedImage;
    }

}
