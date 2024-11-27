package com.janne.imagemanagementservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PathBuilderImpl implements PathBuilder {

    @Value("${files.base-directory}")
    private String baseDirectory;
    @Value("${files.subfolders.thumbnails}")
    private String thumbnailDirectory;
    @Value("${files.subfolders.blurred}")
    private String blurredDirectory;
    @Value("${files.subfolders.originals}")
    private String originalDirectory;
    @Value("${files.upload-extension}")
    private String imageExtension;

    @Override
    public Path buildPathBlurred(String id) {
        return Path.of(baseDirectory + blurredDirectory + "/" + id + "." + imageExtension);
    }

    @Override
    public Path buildPathThumbnail(String id) {
        return Path.of(baseDirectory + thumbnailDirectory + "/" + id + "." + imageExtension);
    }

    @Override
    public Path buildPathOriginal(String id) {
        return Path.of(baseDirectory + originalDirectory + "/" + id + "." + imageExtension);
    }

    @Override
    public Path getThumbnailDirectory() {
        return Path.of(baseDirectory + thumbnailDirectory + "/");
    }

    @Override
    public Path getOriginalDirectory() {
        return Path.of(baseDirectory + originalDirectory + "/");
    }

    @Override
    public Path getBlurredDirectory() {
        return Path.of(baseDirectory + blurredDirectory + "/");
    }

    @Override
    public String getIdFromThumbnailPath(String path) {
        return Arrays.stream(Arrays.stream(path.split("/")).toList().getLast().split("\\.")).toList().getFirst();
    }

    @Override
    public String getIdFromOriginalPath(String path) {
        return Arrays.stream(Arrays.stream(path.split("/")).toList().getLast().split("\\.")).toList().getFirst();
    }

    @Override
    public String getIdFromBlurredPath(String path) {
        return "";
    }

    @Override
    public Path getBaseDirectory() {
        return Path.of(baseDirectory);
    }


}
