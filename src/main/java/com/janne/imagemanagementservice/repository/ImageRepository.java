package com.janne.imagemanagementservice.repository;

import com.janne.imagemanagementservice.model.jpa.ScaledImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ScaledImage, String> {

}
