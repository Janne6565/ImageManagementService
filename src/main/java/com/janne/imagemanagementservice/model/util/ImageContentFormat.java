package com.janne.imagemanagementservice.model.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageContentFormat {
  FULL_SCREEN(1920, 1080),
  VERTICAL_BANNER(456, 100),
  HORIZONTAL_BANNER_V1(480, 1020),
  HORIZONTAL_BANNER_V2(348, 748),
  RECTANGLE_ICON(100, 100),
  RECTANGLE_LARGE(1024, 1024);

  private final int width;
  private final int height;
}
