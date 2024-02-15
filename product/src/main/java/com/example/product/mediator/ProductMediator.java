package com.example.product.mediator;

import com.example.product.exceptions.ObjectDoesntExistException;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.*;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductMediator {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryMediator categoryMediator;
    @Value("${file-service.url}")
    private String FILE_SERVICE;
    public ResponseEntity<?> getProduct(int page, int limit, String name, String category, Float price_min, Float price_max, String date, String sort, String order, String uuid) {

        if (uuid != null && !uuid.isEmpty()){
            return getSingleProduct(uuid);
        }

        if (name != null && !name.isEmpty()){
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return ResponseEntity.status(400).body(new Response("Unsupported encryption format for name"));
            }
        }

        List<ProductEntity> products = productService.getProducts(name, category, price_min, price_max, date, page, limit, sort, order);

        products.forEach( product -> {
            Arrays.stream(product.getImageUrls()).map( image -> FILE_SERVICE + "?uuid=" + image);
        });

//        if (name != null && !name.isEmpty() && date != null && !date.isEmpty()){
//            ProductDTO productDTO = productMapper.createProductDTOFromProductEntity(products.get(0));
//            return ResponseEntity.ok().body(productDTO);
//        }

        List<SimpleProductDTO> simpleProductDTOS = new ArrayList<>();

        long activeProductsCount = productService.countActiveProducts(name, category, price_min, price_max);

        products.forEach( product -> {
            simpleProductDTOS.add(productMapper.createSimpleProductDTOFromProductEntity(product));
        } );
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(activeProductsCount)).body(simpleProductDTOS);
    }

    public ResponseEntity<Response> saveProduct(ProductFormDTO product) {
        try {
            productService.saveProduct(product);
            return ResponseEntity.ok(new Response("Successfully added new product."));
        } catch (ObjectDoesntExistException e){
            return ResponseEntity.status(400).body(new Response("This category doesn't exist."));
        } catch (RuntimeException exception){
            return ResponseEntity.status(400).body(new Response(exception.getMessage()));
        }
    }

    public ResponseEntity<Response> deleteProduct(String uuid) {
        try{
            productService.deleteProduct(uuid);
            return ResponseEntity.ok(new Response("Successfully deleted product."));
        } catch (ObjectDoesntExistException e){
            return ResponseEntity.status(400).body(new Response("This product doesn't exist."));
        }
    }

    public ResponseEntity<?> getSingleProduct(String uuid) {
        try {
            ProductEntity productEntity = productService.getSingleProduct(uuid);
            ProductDTO productDTO = productMapper.createProductDTOFromProductEntity(productEntity);

            return ResponseEntity.ok(productDTO);
        } catch (ObjectDoesntExistException e){
            return ResponseEntity.status(400).body(new Response("This product doesn't exist."));
        }
    }
}
