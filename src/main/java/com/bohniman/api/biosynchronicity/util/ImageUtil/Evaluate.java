package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

public class Evaluate {

    public static JsonResponse evaluateImageForLines(BufferedImage image) throws IOException {

        image = PointRegion.getCroppedRegion(image);
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(image, 0, 0, Color.WHITE, null);
        graphic.dispose();
        // ImageIO.write(result, "png", new File("/Users/bhaskor/MyData/BioSyncUpload/1FileInput.PNG"));
        // ImageIO.write(result, "png", new File("/Users/bhaskor/MyData/BioSyncUpload/2FileGraphics.PNG"));
        result = ImageManipulation.resizeImageWithHint(image, 40, 100);
        // ImageIO.write(result, "png", new File("/Users/bhaskor/MyData/BioSyncUpload/3FileResized.PNG"));
        result = ImageManipulation.getEnhancedBWImage(result);
        // ImageIO.write(result, "png", new File("/Users/bhaskor/MyData/BioSyncUpload/4FileEnhanced.PNG"));
        JsonResponse res = DetectLines.getLineCounts(result);
        if (res.getResult()) {
            System.out.println(res.getMessage());
        } else {
            System.out.println(res.getMessage());
        }

        return res;
    }

}
