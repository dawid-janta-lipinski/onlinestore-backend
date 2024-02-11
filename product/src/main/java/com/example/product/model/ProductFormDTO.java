package com.example.product.model;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFormDTO {

    private String name;
    private String mainDesc;
    private String descHtml;
    private float price;
    private String[] imageUuids;
    private String parameters;
    private String category;
}
