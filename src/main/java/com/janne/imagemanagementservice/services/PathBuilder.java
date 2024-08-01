package com.janne.imagemanagementservice.services;

import java.nio.file.Path;

public interface PathBuilder {
    Path buildPathThumbnail(String id);

    Path buildPathOriginal(String id);

    Path getThumbnailDirectory();

    Path getOriginalDirectory();

    String getIdFromThumbnailPath(String path);

    String getIdFromOriginalPath(String path);

    Path getBaseDirectory();
}
