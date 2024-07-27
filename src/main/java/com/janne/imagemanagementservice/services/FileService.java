package com.janne.imagemanagementservice.services;

import com.janne.imagemanagementservice.exceptions.CorruptedImagesException;
import com.janne.imagemanagementservice.model.jpa.ScaledImage;
import com.janne.imagemanagementservice.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileService {

    private final PathBuilder pathBuilder;
    private final ImageUtilityService imageUtilityService;
    @Value("${files.upload-extension}")
    private String imageExtension;

    public boolean doesImageExist(String id) {
        Path originalPath = pathBuilder.buildPathOriginal(id);
        Path thumbnailPath = pathBuilder.buildPathThumbnail(id);
        boolean originalExists = Files.exists(originalPath) && Files.isRegularFile(originalPath);
        boolean thumbnailExists = Files.exists(thumbnailPath) && Files.isRegularFile(thumbnailPath);

        if (originalExists && thumbnailExists) {
            return true;
        } else {
            if (originalExists) {
                throw new CorruptedImagesException("Thumbnail is missing for image with id: " + id);
            }
            if (thumbnailExists) {
                throw new CorruptedImagesException("Original is missing for image with id: " + id);
            }
            return false;
        }
    }

    public boolean doesDirectoryExist(String path) {
        return Files.exists(Path.of(path));
    }

    public void createDirectory(String path) {
        Path pathToCreate = Path.of(path);

        if (doesDirectoryExist(path)) {
            return;
        }

        try {
            Files.createDirectories(pathToCreate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImage(String id) {
        Path originalPath = pathBuilder.buildPathOriginal(id);
        Path thumbnailPath = pathBuilder.buildPathThumbnail(id);

        try {
            Files.deleteIfExists(originalPath);
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void uploadImage(BufferedImage image, String id) {
        Path pathOriginal = pathBuilder.buildPathOriginal(id);
        Path pathThumbnail = pathBuilder.buildPathThumbnail(id);
        File originalFile = pathOriginal.toFile();
        File thumbnailFile = pathThumbnail.toFile();
        BufferedImage downscaledImage = imageUtilityService.scaleImage(image, 0.3f);

        try {
            originalFile.createNewFile();
            thumbnailFile.createNewFile();
            ImageIO.write(image, imageExtension, originalFile);
            ImageIO.write(downscaledImage, imageExtension, thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
