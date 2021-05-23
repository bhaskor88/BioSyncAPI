package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PointRegion {

    final static int VARIABLE_HEIGHT = 369;
    final static int VARIABLE_WIDTH = 525;
    final static int CROP_X_CORD = 40;
    final static int CROP_Y_CORD = 268;

    public static BufferedImage getCroppedRegion(BufferedImage image) {
        try {
            // BufferedImage image = ImageIO.read(image2);
            BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);
            graphic.dispose();

            BufferedImage pointImage = ImageManipulation.resizeImageWithHint(result, image.getWidth(),
                    image.getHeight());

            // pointImages.add(pointImage);

            pointImage = crop(pointImage, image);

            return pointImage;
        } catch (Exception ex) {
            Logger.getLogger(PointRegion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static BufferedImage crop(BufferedImage pointImage, BufferedImage original) {

        int[] cropCords = new int[4];

        try {

            pointImage = ImageManipulation.resizeImageWithHint(pointImage, 75, 375);
            int black_counts_found = 0;
            for (int y = 80; y < pointImage.getHeight() - 1; y++) {

                for (int x = 12; x < pointImage.getWidth() / 2; x++) {
                    // Retrieving contents of a pixel
                    int pixel = pointImage.getRGB(x, y);
                    // Creating a Color object from pixel value
                    Color color = new Color(pixel, true);

                    // Retrieving the R G B values
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    if ((red >= 0 && red <= 25) && (green >= 0 && green <= 25) && (blue >= 0 && blue <= 25)) {
                        if (black_counts_found == 0) {
                            cropCords[0] = x;
                            cropCords[1] = y;
                        }
                        black_counts_found++;
                        break;
                    }

                }
            }

            black_counts_found = 0;
            for (int y = 80; y < pointImage.getHeight() / 2; y++) {
                for (int x = cropCords[0] + 15; x < cropCords[0] + 35; x++) {
                    // Retrieving contents of a pixel
                    int pixel = pointImage.getRGB(x, y);
                    // Creating a Color object from pixel value
                    Color color = new Color(pixel, true);

                    // Retrieving the R G B values
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    if ((red >= 0 && red <= 50) && (green >= 0 && green <= 50) && (blue >= 0 && blue <= 50)) {
                        if (black_counts_found == 0) {
                            cropCords[2] = x - cropCords[0];
                        }
                        black_counts_found++;
                    }
                }
            }
            black_counts_found = 0;
            for (int y = pointImage.getHeight() - 135; y > 80; y--) {
                for (int x = 12; x < pointImage.getWidth() - 1; x++) {
                    // Retrieving contents of a pixel
                    int pixel = pointImage.getRGB(x, y);
                    // Creating a Color object from pixel value
                    Color color = new Color(pixel, true);
                    // Retrieving the R G B values
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    if ((red >= 0 && red <= 25) && (green >= 0 && green <= 25) && (blue >= 0 && blue <= 25)) {
                        if (black_counts_found == 0) {
                            cropCords[3] = y - cropCords[1];
                        }
                        black_counts_found++;
                    }
                }
            }
            black_counts_found = 0;

            BufferedImage tempImage = ImageManipulation.resizeImageWithHint(original, 75, 375);
            pointImage = tempImage.getSubimage(cropCords[0], cropCords[1], cropCords[2], cropCords[3]);
            pointImage = ImageManipulation.resizeImageWithHint(pointImage, 30, 100);

            int second_x = cropCords[0] + 10;
            int second_y = cropCords[1] + 10;
            int second_w = 5;
            int second_h = cropCords[3] - 20;
            pointImage = tempImage.getSubimage(second_x, second_y, second_w, second_h);
            pointImage = ImageManipulation.resizeImageWithHint(pointImage, 30, 100);
            return pointImage;
        } catch (Exception ex) {
            Logger.getLogger(PointRegion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
