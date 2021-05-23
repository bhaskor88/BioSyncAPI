package com.bohniman.api.biosynchronicity.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import com.bohniman.api.biosynchronicity.util.ImageUtil.Evaluate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DummyController {

    @PostMapping("/auth/evaluateImage")
    public ResponseEntity<?> evaluateImage(@RequestParam("testImage") MultipartFile file) throws IOException {
        InputStream is = new ByteArrayInputStream(file.getBytes());
        BufferedImage image = ImageIO.read(is);

        return ResponseEntity.ok(Evaluate.evaluateImageForLines(image));

    }

}
