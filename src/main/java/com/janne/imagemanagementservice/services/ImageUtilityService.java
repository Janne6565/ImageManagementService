package com.janne.imagemanagementservice.services;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ImageUtilityService {

    public BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(image, 0, 0, width, height, null);
        return newImage;
    }

    public BufferedImage scaleImage(BufferedImage image, float scale) {
       int width = (int) (image.getWidth() * scale);
       int height = (int) (image.getHeight() * scale);
         return scaleImage(image, width, height);
    }

}
