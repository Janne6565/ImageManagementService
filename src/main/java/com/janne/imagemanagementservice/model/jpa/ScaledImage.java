package com.janne.imagemanagementservice.model.jpa;

import jakarta.persistence.*;
import lombok.*;

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
    @NonNull
    private Time uploadedAt;
}
