package com.janne.imagemanagementservice.model.util;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageContentFormat {
    FULL_SCREEN(1920, 1080, 0.6f),
    RECTANGLE_ICON(100, 100, 0.6f);

    private final int width;
    private final int height;
    private final float thumbnailScaleFactor;
}
