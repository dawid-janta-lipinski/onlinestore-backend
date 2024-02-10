package com.example.file.dao;

import com.example.file.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageDao extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findImageEntitiesByUuid(String uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM images WHERE created_at < current_date - interval '2 days' and is_used = false")
    List<ImageEntity> findUnusedImages();
}
