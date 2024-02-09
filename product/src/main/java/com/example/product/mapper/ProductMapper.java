package com.example.product.mapper;

import com.example.product.dao.CategoryDao;
import com.example.product.mediator.ProductMediator;
import com.example.product.model.ProductDTO;
import com.example.product.model.ProductEntity;
import com.example.product.model.SimpleProductDTO;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public SimpleProductDTO createSimpleProductDTOFromProductEntity(ProductEntity productEntity){
        return new SimpleProductDTO(
                productEntity.getName(),
                productEntity.getMainDesc(),
                productEntity.getPrice(),
                productEntity.getImageUrls()[0],
                productEntity.getCreateAt()
        );
    }


    public ProductDTO createProductDTOFromProductEntity(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getUid(),
                productEntity.isActive(),
                productEntity.getName(),
                productEntity.getMainDesc(),
                productEntity.getDescHtml(),
                productEntity.getPrice(),
                productEntity.getImageUrls(),
                productEntity.getParameters(),
                productEntity.getCreateAt(),
                categoryMapper.createCategoryDTOFromCategoryEntity(productEntity.getCategory())
                );
    }
//    public ProductEntity createProductEntityFromProductDTO(ProductDTO productDTO) {
//        return new ProductEntity(
//                UUID.randomUUID().toString(),
//                productDTO.isActive(),
//                productDTO.getName(),
//                productDTO.getMainDesc(),
//                productDTO.getDescHtml(),
//                productDTO.getPrice(),
//                productDTO.getImageUrls(),
//                productDTO.getParameters(),
//                productDTO.getCreatedAt(),
//                categoryDao.findByShortId(ProductDTO);
//    }
}