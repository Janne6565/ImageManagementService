package com.janne.imagemanagementservice.services;

import java.nio.file.Path;

public interface PathBuilder {
  Path buildPathThumbnail(String id);

  Path buildPathOriginal(String id);

  Path buildPathBlurred(String id);

  Path getThumbnailDirectory();

  Path getOriginalDirectory();

  Path getBlurredDirectory();

  String getIdFromThumbnailPath(String path);

  String getIdFromOriginalPath(String path);

  String getIdFromBlurredPath(String path);

  Path getBaseDirectory();

}
