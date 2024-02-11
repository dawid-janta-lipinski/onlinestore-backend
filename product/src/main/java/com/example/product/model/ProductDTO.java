package com.example.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductDTO extends Product{
    private CategoryDTO categoryDTO;

    public ProductDTO(String uuid, boolean active, String name, String mainDesc, String descHtml, float price, String[] imageUrls, String parameters, LocalDate createdAt, CategoryDTO categoryDTO) {
        super(uuid, active, name, mainDesc, descHtml, price, imageUrls, parameters, createdAt);
        this.categoryDTO = categoryDTO;
    }
}
