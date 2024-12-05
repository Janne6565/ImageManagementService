package com.janne.imagemanagementservice.model.dto;

import com.janne.imagemanagementservice.model.util.ImageContentFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageLinkDto {
  private String id;
  private String fullSizeUrl;
  private String thumbnailUrl;
  private String blurredUrl;
  private ImageContentFormat format;
}
