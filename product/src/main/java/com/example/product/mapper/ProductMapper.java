package com.example.product.mapper;

import com.example.product.dao.CategoryDao;
import com.example.product.exceptions.ObjectDoesntExistException;
import com.example.product.mediator.ProductMediator;
import com.example.product.model.ProductDTO;
import com.example.product.model.ProductEntity;
import com.example.product.model.ProductFormDTO;
import com.example.product.model.SimpleProductDTO;
import com.example.product.service.CategoryService;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

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
                productEntity.getUuid(),
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
    public ProductFormDTO createProductFormDTOFromProductEntity(ProductEntity productEntity) {
        return new ProductFormDTO(
                productEntity.getName(),
                productEntity.getMainDesc(),
                productEntity.getDescHtml(),
                productEntity.getPrice(),
                productEntity.getImageUrls(),
                productEntity.getParameters(),
                productEntity.getCategory().getShortId()
        );
    }
    public ProductEntity createProductEntityFromProductFormDTO(ProductFormDTO productFormDTO) throws ObjectDoesntExistException {
        return new ProductEntity(
                UUID.randomUUID().toString(),
                true,
                productFormDTO.getName(),
                productFormDTO.getMainDesc(),
                productFormDTO.getDescHtml(),
                productFormDTO.getPrice(),
                productFormDTO.getImageUuids(),
                productFormDTO.getParameters(),
                LocalDate.now(),
                categoryService.findCategoryByShortId(productFormDTO.getCategory())
        );
    }
}