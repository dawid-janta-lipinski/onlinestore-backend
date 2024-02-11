package com.example.product.controller;

import com.example.product.mediator.CategoryMediator;
import com.example.product.model.CategoryDTO;
import com.example.product.model.ProductDTO;
import com.example.product.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMediator categoryMediator;

    @GetMapping
    public ResponseEntity<?> getCategories(){
        return categoryMediator.getCategories();
    }

    @PostMapping()
    public ResponseEntity<Response> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryMediator.createCategory(categoryDTO);
    }
}
