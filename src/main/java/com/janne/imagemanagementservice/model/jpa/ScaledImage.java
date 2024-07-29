package com.janne.imagemanagementservice.model.jpa;

import com.janne.imagemanagementservice.model.util.ImageContentFormat;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "scaled_images")
public class ScaledImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotNull
    private Time uploadedAt;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ImageContentFormat format;
}
