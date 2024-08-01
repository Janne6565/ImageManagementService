package com.janne.imagemanagementservice.services;

import com.janne.imagemanagementservice.model.dto.ImageLinkDto;
import com.janne.imagemanagementservice.model.jpa.ScaledImage;
import com.janne.imagemanagementservice.model.util.ImageContentFormat;
import com.janne.imagemanagementservice.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final PathBuilder pathBuilder;
    private final ImageUtilityService imageUtilityService;
    private final ImageValidationService imageValidationService;

    @PostConstruct
    public void init() {
        fileService.createDirectory(pathBuilder.getThumbnailDirectory().toString());
        fileService.createDirectory(pathBuilder.getOriginalDirectory().toString());
    }

    public void deleteImage(String id) throws FileNotFoundException {
        if (!doesImageExist(id)) {
            throw new FileNotFoundException("Image with id " + id + " not found");
        }
        fileService.deleteImage(id);
        imageRepository.deleteById(id);
    }

    public ImageLinkDto uploadImage(BufferedImage image, ImageContentFormat format) {
        BufferedImage baseImage = imageUtilityService.scaleImage(image, format);
        ScaledImage scaledImage = imageRepository.save(ScaledImage.builder()
                .uploadedAt(new Time(new Date().getTime()))
                .format(format)
                .build());


        ImageLinkDto imageLinkDto = fileService.uploadImage(baseImage, scaledImage.getId());
        imageLinkDto.setFormat(format);
        return imageLinkDto;
    }

    public ImageLinkDto getImage(String id) {
        if (imageRepository.findById(id).isEmpty()) {
            return null;
        }

        return ImageLinkDto.builder()
                .fullSizeUrl(pathBuilder.buildPathOriginal(id).toString())
                .thumbnailUrl(pathBuilder.buildPathThumbnail(id).toString())
                .format(imageRepository.findById(id).get().getFormat())
                .build();
    }

    public boolean doesImageExist(String id) {
        if (!imageRepository.existsById(id)) {
            return false;
        }
        return !fileService.isImageInvalid(id);
    }

    /**
     * Removes all Images from the persistent datasource which don't exist locally
     * and deletes all local images that don't exist in the database
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void cleanupImages() {
        Set<String> inDatabaseExistingIds = new HashSet<>(imageRepository.findAll().stream().map(ScaledImage::getId).toList());
        fileService.cleanupFiles();

        for (String id : inDatabaseExistingIds) {
            if (fileService.isImageInvalid(id)) {
                imageRepository.deleteById(id);
            }
        }

        for (String id : fileService.getAllImageIds()) {
            if (!inDatabaseExistingIds.contains(id)) {
                fileService.deleteImage(id);
            }
        }
    }
}
