package com.example.product.dao;

import com.example.product.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findCategoryByName(String name);

    Optional<CategoryEntity> findCategoryByShortId(String category);
}
