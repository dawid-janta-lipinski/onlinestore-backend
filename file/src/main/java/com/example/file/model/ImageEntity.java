package com.example.file.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "images")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue(generator = "images_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "images_id_seq",sequenceName = "images_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    @Column(name = "file_path")
    private String path;
    @Column(name = "is_used")
    private boolean isUsed;
    @Column(name = "created_at")
    private LocalDate createdAt;

}
