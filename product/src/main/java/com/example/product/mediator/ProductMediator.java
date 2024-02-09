package com.example.product.mediator;

import com.example.product.mapper.ProductMapper;
import com.example.product.model.ProductDTO;
import com.example.product.model.ProductEntity;
import com.example.product.model.Response;
import com.example.product.model.SimpleProductDTO;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductMediator {
    private final ProductService productService;
    private final ProductMapper productMapper;
    public ResponseEntity<?> getProduct(int page, int limit, String name, String category, Float price_min, Float price_max, String date, String sort, String order) {
        List<ProductEntity> products = productService.getProducts(name, category, price_min, price_max, date, page, limit, sort, order);

        if (name != null && !name.isEmpty() && date != null && !date.isEmpty()){
            ProductDTO productDTO = productMapper.createProductDTOFromProductEntity(products.get(0));
            return ResponseEntity.ok().body(productDTO);
        }

        List<SimpleProductDTO> simpleProductDTOS = new ArrayList<>();

        long activeProductsCount = productService.countActiveProducts(name, category, price_min, price_max);

        products.forEach( product -> {
            simpleProductDTOS.add(productMapper.createSimpleProductDTOFromProductEntity(product));
        } );
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(activeProductsCount)).body(simpleProductDTOS);
    }
}
