package com.janne.imagemanagementservice.exceptions;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestException extends RuntimeException {
  private String reason;
  private int code;
  private String message;
}
