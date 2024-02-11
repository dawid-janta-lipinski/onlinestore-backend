package com.example.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity()
@Table(name = "products")
public class ProductEntity extends Product {
    @Id
    @GeneratedValue(generator = "products_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "products_id_seq",sequenceName = "products_id_seq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    public ProductEntity(String uuid, boolean active, String name, String mainDesc, String descHtml, float price, String[] imageUrls, String parameters, LocalDate createdAt, CategoryEntity category) {
        super(uuid, active, name, mainDesc, descHtml, price, imageUrls, parameters, createdAt);
        this.category = category;
    }
}
