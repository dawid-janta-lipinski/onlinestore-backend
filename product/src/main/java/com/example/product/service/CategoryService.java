package com.example.product.service;

import com.example.product.dao.CategoryDao;
import com.example.product.exceptions.ObjectExistsInDatabaseException;
import com.example.product.mapper.CategoryMapper;
import com.example.product.model.CategoryDTO;
import com.example.product.model.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    public List<CategoryEntity> getCategories() {
        return categoryDao.findAll();
    }

    public void createCategory(CategoryDTO categoryDTO) throws ObjectExistsInDatabaseException {
        categoryDao.findCategoryByName(categoryDTO.getName()).ifPresent(categoryEntity -> {
                    throw new ObjectExistsInDatabaseException("Category Already Exists!");
        });

        categoryDao.save( new CategoryEntity(
                categoryDTO.getName(),
                UUID.randomUUID().toString().replace("-","").substring(0,12)
        )
        );
    }
}
