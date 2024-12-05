package com.janne.imagemanagementservice.exceptions;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadUploadException extends RuntimeException {
  private String reason;
}
