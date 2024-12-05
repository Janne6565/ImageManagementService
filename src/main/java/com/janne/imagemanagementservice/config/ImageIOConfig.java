package com.janne.imagemanagementservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;

@Configuration
public class ImageIOConfig {

  @PostConstruct
  public void init() {
    ImageIO.scanForPlugins();
  }
}
