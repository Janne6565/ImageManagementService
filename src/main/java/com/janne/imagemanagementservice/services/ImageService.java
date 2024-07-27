package com.janne.imagemanagementservice.services;

import com.janne.imagemanagementservice.model.dto.ImageLinkDto;
import com.janne.imagemanagementservice.model.jpa.ScaledImage;
import com.janne.imagemanagementservice.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final PathBuilder pathBuilder;

    @PostConstruct
    public void init() {
        fileService.createDirectory(pathBuilder.getThumbnailDirectory().toString());
        fileService.createDirectory(pathBuilder.getOriginalDirectory().toString());
    }

    public void deleteImage(String id) {
        fileService.deleteImage(id);
        imageRepository.deleteById(id);
    }

    public ScaledImage uploadImage(BufferedImage image) {
        ScaledImage scaledImage = imageRepository.save(ScaledImage.builder()
                .uploadedAt(new Time(new Date().getTime()))
                .build());
        String uuid = scaledImage.getId();
        fileService.uploadImage(image, uuid);
        return scaledImage;
    }

    public ImageLinkDto getImage(String id) {
        return ImageLinkDto.builder()
                .fullSizeUrl(pathBuilder.buildPathOriginal(id).toString())
                .thumbnailUrl(pathBuilder.buildPathThumbnail(id).toString())
                .build();
    }

    public boolean doesImageExist(String id) {
        if (!imageRepository.existsById(id)) {
            return false;
        }
        return fileService.doesImageExist(id);
    }
}
