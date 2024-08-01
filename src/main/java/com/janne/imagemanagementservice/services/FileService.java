package com.janne.imagemanagementservice.services;

import com.janne.imagemanagementservice.exceptions.CorruptedImagesException;
import com.janne.imagemanagementservice.model.dto.ImageLinkDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final PathBuilder pathBuilder;
    private final ImageUtilityService imageUtilityService;
    @Value("${files.upload-extension}")
    private String imageExtension;

    public boolean doesImageExist(String id) throws CorruptedImagesException {
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
        } catch (IOException e) {
            log.debug("cant delete original for image: {}", id);
        }

        try {
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException e) {
            log.debug("cant delete thumbnail for image: {}", id);
        }
    }

    private BufferedImage optimizeImage(BufferedImage image) throws IOException {
        return Thumbnails.of(image)
                .outputQuality(0.7)
                .scale(1)
                .asBufferedImage();
    }

    @SneakyThrows
    public ImageLinkDto uploadImage(BufferedImage image, String id) {
        image = optimizeImage(image);
        Path pathOriginal = pathBuilder.buildPathOriginal(id);
        Path pathThumbnail = pathBuilder.buildPathThumbnail(id);
        File originalFile = pathOriginal.toFile();
        File thumbnailFile = pathThumbnail.toFile();
        BufferedImage downscaledImage = imageUtilityService.scaleImage(image, 0.3f);

        try {
            assert !originalFile.createNewFile() : "Original file already exists";
            assert !thumbnailFile.createNewFile() : "Thumbnail file already exists";
            ImageIO.write(image, imageExtension, originalFile);
            ImageIO.write(downscaledImage, imageExtension, thumbnailFile);
            return ImageLinkDto.builder()
                    .fullSizeUrl(pathOriginal.toString())
                    .thumbnailUrl(pathThumbnail.toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This methode does not check if the images are valid and just returns all original ImageIds in the directory
     *
     * @return List of all IDs from existing Images which could be found in the "original" image directory
     */
    public List<String> getAllImageIds() {
        try {
            try (Stream<Path> paths = Files.list(pathBuilder.getOriginalDirectory())) {
                return paths.map(path -> pathBuilder.getIdFromOriginalPath(path.toString()))
                        .toList();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isImageInvalid(String id) {
        try {
            Path originalPath = pathBuilder.buildPathOriginal(id);
            Path thumbnailPath = pathBuilder.buildPathThumbnail(id);
            BufferedImage originalImage = ImageIO.read(originalPath.toFile());
            BufferedImage thumbnailImage = ImageIO.read(thumbnailPath.toFile());
            return originalImage == null || thumbnailImage == null;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * delete all files locally which either:
     * - don't have an original image
     * - don't have a thumbnail image
     * - can't be read as images
     */
    public void cleanupFiles() {
        try {
            try (Stream<Path> paths = Files.list(pathBuilder.getThumbnailDirectory())) {
                paths.forEach(path -> {
                    String id = pathBuilder.getIdFromThumbnailPath(path.toString());
                    if (isImageInvalid(id)) {
                        deleteImage(id);
                    }
                });
            }

            try (Stream<Path> paths = Files.list(pathBuilder.getOriginalDirectory())) {
                paths.forEach(path -> {
                    String id = pathBuilder.getIdFromOriginalPath(path.toString());
                    if (isImageInvalid(id)) {
                        deleteImage(id);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
