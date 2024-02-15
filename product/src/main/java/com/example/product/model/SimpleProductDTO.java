package com.example.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleProductDTO {
    private String uuid;
    private String name;
    private String mainDesc;
    private float price;
    private String imageUrl;
    private LocalDate createdAt;
}
