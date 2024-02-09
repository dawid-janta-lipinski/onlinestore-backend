package com.example.product.mediator;

import com.example.product.dao.CategoryDao;
import com.example.product.exceptions.ObjectExistsInDatabaseException;
import com.example.product.mapper.CategoryMapper;
import com.example.product.model.CategoryDTO;
import com.example.product.model.CategoryEntity;
import com.example.product.model.Response;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CategoryMediator {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    public ResponseEntity<?> getCategories() {
        List<CategoryDTO> categories = categoryService.getCategories()
                .stream()
                .map(categoryMapper::createCategoryDTOFromCategoryEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }

    public ResponseEntity<Response> createCategory(CategoryDTO categoryDTO) {
        try {
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(new Response("Category successfully added."));
        } catch (ObjectExistsInDatabaseException e){
            return ResponseEntity.ok(new Response("This category already exists."));
        }
    }
}
