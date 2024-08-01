package com.janne.imagemanagementservice.services;

import com.janne.imagemanagementservice.exceptions.BadUploadException;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class ImageValidationService {

    @Value("${files.validation.file-types}")
    private String validFileTypes;
    @Value("${files.validation.max-file-size}")
    private long maxUploadFileSize;

    private Set<String> acceptedFileTypes;

    @PostConstruct
    private void init() {
        acceptedFileTypes = new HashSet<>(Arrays.stream(validFileTypes.split(", ")).toList());
    }

    private String isFormatValid(MultipartFile multipartFile) {
        return acceptedFileTypes.contains(multipartFile.getContentType()) ? null : "Format of the uploaded file is invalid";
    }

    private String isFileSizeValid(MultipartFile multipartFile) {
        return multipartFile.getSize() <= maxUploadFileSize ? null : "Uploaded file is to large";
    }

    public void validateImage(MultipartFile multipartFile) throws BadUploadException {
        MultiPartFileChecker.builder().file(multipartFile).build()
                .check(this::isFileSizeValid)
                .check(this::isFormatValid);
    }

    @Builder
    private record MultiPartFileChecker(MultipartFile file) {
        public MultiPartFileChecker check(Function<MultipartFile, String> checkFunction) {
            String res = checkFunction.apply(file);
            if (res == null) {
                return this;
            }

            throw BadUploadException.builder()
                    .reason(res)
                    .build();

        }

    }
}
