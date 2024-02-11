package com.example.product.dao;

import com.example.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<ProductEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE product_name=? 1 AND create_at =? 2")
    List<ProductEntity> findByNameAndCreateAt(String name, LocalDate date);

    Optional<ProductEntity> findByUuid(String uuid);
}
