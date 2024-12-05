package com.janne.imagemanagementservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  @GetMapping("/")
  public ResponseEntity<String> getBase() {
    return ResponseEntity.ok("Hello World!");
  }
}
