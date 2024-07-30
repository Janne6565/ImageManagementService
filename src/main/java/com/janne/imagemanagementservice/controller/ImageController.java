package com.janne.imagemanagementservice.controller;

import com.janne.imagemanagementservice.model.dto.ImageLinkDto;
import com.janne.imagemanagementservice.model.jpa.ScaledImage;
import com.janne.imagemanagementservice.model.util.ImageContentFormat;
import com.janne.imagemanagementservice.services.ImageService;
import com.janne.imagemanagementservice.services.ImageValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageValidationService imageValidationService;

    @PostMapping
    public ResponseEntity<ScaledImage> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("format") ImageContentFormat format) {
        imageValidationService.validateImage(file);
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            ScaledImage scaledImage = imageService.uploadImage(image, format);
            return ResponseEntity.ok(scaledImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageLinkDto> getImage(@PathVariable String id) {
        if (!imageService.doesImageExist(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageService.getImage(id));
    }
}