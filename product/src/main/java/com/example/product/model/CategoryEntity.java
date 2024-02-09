package com.example.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(generator = "categories_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "categories_id_seq",sequenceName = "categories_id_seq", allocationSize = 1)
    private long id;
    @Column(name = "category_name")
    private String name;
    private String shortId;

    public CategoryEntity(String name, String shortId) {
        this.name = name;
        this.shortId = shortId;
    }
}
