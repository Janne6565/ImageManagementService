package com.janne.imagemanagementservice.exceptions;

public class CorruptedImagesException extends RuntimeException {
    public CorruptedImagesException(String message) {
        super(message);
    }
}
