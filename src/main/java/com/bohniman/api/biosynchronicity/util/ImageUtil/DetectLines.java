package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

public class DetectLines {

    public static JsonResponse getLineCounts(BufferedImage originalImage) {

        JsonResponse res = new JsonResponse();

        int lineCount = 0;

        int model_consecutive_pixelCount = 5;
        int model_consecutive_pixel_gaping = 2;

        int prev_Y_cord = 0;
        int consecutive_pixelCounting_line1 = 0;
        int consecutive_pixelCounting_line2 = 0;

        int totalpixelCount = 0;
        int totalBCount = 0;
        for (int y = 20; y < originalImage.getHeight() - 20; y++) {
            for (int x = 5; x < originalImage.getWidth() - 15; x++) {
                int pixel = originalImage.getRGB(x, y);
                totalpixelCount++;
                if ((pixel & 0x00FFFFFF) == 0) {
                    totalBCount++;
                }
            }
        }

        System.out.println("Total = " + totalpixelCount);
        System.out.println("Blacks = " + totalBCount);

        if (!(totalBCount / totalpixelCount * 100 > 33.33)) {
            for (int y = 20; y < originalImage.getHeight() / 2; y++) {
                for (int x = 5; x < originalImage.getWidth() - 15; x++) {
                    int pixel = originalImage.getRGB(x, y);
                    if ((pixel & 0x00FFFFFF) == 0) {
                        if (consecutive_pixelCounting_line1 == 0) {
                            prev_Y_cord = y;
                            consecutive_pixelCounting_line1++;
                        } else {
                            if (Math.abs(prev_Y_cord - y) < model_consecutive_pixel_gaping) {
                                consecutive_pixelCounting_line1++;
                                Color color = new Color(255, 0, 0);
                                originalImage.setRGB(x, y, color.getRGB());
                            }
                        }
                    }
                }
            }
            if (consecutive_pixelCounting_line1 >= model_consecutive_pixelCount) {
                lineCount++;
            }
            for (int y = originalImage.getHeight() / 2; y < originalImage.getHeight() - 20; y++) {
                for (int x = 5; x < originalImage.getWidth() - 15; x++) {
                    int pixel = originalImage.getRGB(x, y);
                    if ((pixel & 0x00FFFFFF) == 0) {
                        if (consecutive_pixelCounting_line2 == 0) {
                            prev_Y_cord = y;
                            consecutive_pixelCounting_line2++;
                        } else {
                            if (Math.abs(prev_Y_cord - y) < model_consecutive_pixel_gaping) {
                                consecutive_pixelCounting_line2++;
                                Color color = new Color(255, 0, 0);
                                originalImage.setRGB(x, y, color.getRGB());
                            }
                        }

                    }
                }
            }
            if (consecutive_pixelCounting_line2 >= model_consecutive_pixelCount) {
                lineCount++;
            }
            res.setPayload(lineCount);
            res.setMessage("No of Lines Detected - " + lineCount);
            res.setResult(true);
            return res;
        }
        // Else Not to dark
        res.setMessage("Unhandled Exception Occured..");
        res.setResult(false);
        return res;
    }

}
