package com.janne.imagemanagementservice.model.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageContentFormat {
    FULL_SCREEN(1920, 1080, 0.6f),
    VERTICAL_BANNER(456, 100, 0.6f),
    HORIZONTAL_BANNER_V1(480, 1020, 0.6f),
    HORIZONTAL_BANNER_V2(348, 748, 0.6f),
    RECTANGLE_ICON(100, 100, 0.6f),
    RECTANGLE_LARGE(1024, 1024, 0.6f);

    private final int width;
    private final int height;
    private final float thumbnailScaleFactor;
}
