package com.example.product.mapper;

import com.example.product.model.CategoryDTO;
import com.example.product.model.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO createCategoryDTOFromCategoryEntity(CategoryEntity category) {
        return new CategoryDTO(
                category.getName(),
                category.getShortId()
        );
    }
    public CategoryEntity createCategoryEntityFromCategoryDTO(CategoryDTO category) {
        return new CategoryEntity(
                category.getName(),
                category.getShortId()
        );
    }
}
