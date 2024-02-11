package com.example.product.controller;

import com.example.product.dao.ProductDao;
import com.example.product.mediator.ProductMediator;
import com.example.product.model.Product;
import com.example.product.model.ProductDTO;
import com.example.product.model.ProductFormDTO;
import com.example.product.model.Response;
import com.example.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductMediator productMediator;

    @GetMapping
    public ResponseEntity<?> get(HttpServletRequest request,
                                 @RequestParam(required = false) String name_like,
                                 @RequestParam(required = false) String date,
                                 @RequestParam(required = false) String _category,
                                 @RequestParam(required = false) Float price_min,
                                 @RequestParam(required = false) Float price_max,
                                 @RequestParam(required = false, defaultValue = "1") int _page,
                                 @RequestParam(required = false, defaultValue = "10") int _limit,
                                 @RequestParam(required = false,defaultValue = "price") String _sort,
                                 @RequestParam(required = false,defaultValue = "asc") String _order){
        return productMediator.getProduct(_page, _limit, name_like, _category, price_min, price_max, date, _sort,_order);
    }

    @PostMapping()
    public ResponseEntity<Response> save(@RequestBody ProductFormDTO product){
        return productMediator.saveProduct(product);
    }
    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestParam String uuid){
        return productMediator.deleteProduct(uuid);
    }
}
