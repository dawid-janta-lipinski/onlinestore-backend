package com.example.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Product {
    private String uuid;
    private boolean active;
    @Column(name = "product_name")
    private String name;
    private String mainDesc;
    private String descHtml;
    private float price;
    private String[] imageUrls;
    private String parameters;
    private LocalDate createAt;

    public Product(String uuid, boolean active, String name, String mainDesc, String descHtml, float price, String[] imageUrls, String parameters, LocalDate createdAt) {
        this.uuid = uuid;
        this.active = active;
        this.name = name;
        this.mainDesc = mainDesc;
        this.descHtml = descHtml;
        this.price = price;
        this.imageUrls = imageUrls;
        this.parameters = parameters;
        this.createAt = createdAt;
    }
}
