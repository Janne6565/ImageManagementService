package com.janne.imagemanagementservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadUploadException.class)
  public ResponseEntity<ExceptionResponse> badUploadHandler(BadUploadException exception) {
    return ResponseEntity.badRequest().body(
      ExceptionResponse.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .message("Uploaded file does not conform to specified image restrictions")
        .reason(exception.getReason())
        .build()
    );
  }

  @ExceptionHandler(RequestException.class)
  public ResponseEntity<ExceptionResponse> globalExceptionHandler(RequestException exception) {
    return ResponseEntity.status(exception.getCode()).body(
      ExceptionResponse.builder()
        .code(exception.getCode())
        .message(exception.getMessage())
        .reason(exception.getReason())
        .build()
    );
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ExceptionResponse> imageMissingInRequest(MissingServletRequestPartException exception) {
    return ResponseEntity.badRequest()
      .body(
        ExceptionResponse.builder()
          .code(HttpStatus.BAD_REQUEST.value())
          .message("Image missing")
          .reason("No Image provided in Request Form-Body")
          .build()
      );
  }
}
