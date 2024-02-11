package com.example.product.service;

import com.example.product.exceptions.ObjectDoesntExistException;
import com.example.product.model.ProductFormDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import com.example.product.dao.CategoryDao;
import com.example.product.dao.ProductDao;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final ProductMapper productMapper;

    @Value("${file-service.url}")
    private String FILE_SERVICE;


    public long countActiveProducts(String name, String category, Float price_min, Float price_max){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);
        List<Predicate> predicates = prepareQuery(name,category,price_min,price_max,criteriaBuilder,root);
        query.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getSingleResult();

    }

    public List<ProductEntity> getProducts(String name,
                                           String category,
                                           Float price_min,
                                           Float price_max,
                                           String date,
                                           int page,
                                           int limit,
                                           String sort,
                                           String order) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);

        if (date != null && !date.equals("") && name != null && !name.trim().equals("")){
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return productDao.findByNameAndCreateAt(name, localDate);
        }
        if (page <= 0) page = 1;

        List<Predicate> predicates = prepareQuery(name, category, price_min, price_max, criteriaBuilder, root);

        if (!order.isEmpty() && !sort.isEmpty()){
            String column = null;
            switch (sort){
                case "name":
                    column="name";
                    break;
                case "category":
                    column = "category";
                    break;
                case "date":
                    column = "createAt";
                    break;
                default:
                    column="price";
                    break;
            }
            Order orderQuery;
            if (order.equals("desc")){
                orderQuery =  criteriaBuilder.desc(root.get(column));
            }else {
                orderQuery =  criteriaBuilder.asc(root.get(column));
            }
            query.orderBy(orderQuery);
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setFirstResult((page-1)*limit).setMaxResults(limit).getResultList();


    }

    private List<Predicate> prepareQuery(String name,
                                         String category,
                                         Float price_min,
                                         Float price_max,
                                         CriteriaBuilder criteriaBuilder,
                                         Root<ProductEntity> root){
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.trim().equals("")) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (category != null && !category.equals("")) {
            categoryDao.findCategoryByShortId(category)
                    .ifPresent(value-> predicates.add(criteriaBuilder.equal(root.get("category"), value)));
        }
        if (price_min != null) {
            predicates.add(criteriaBuilder.greaterThan(root.get("price"), price_min-0.01));
        }
        if (price_max != null) {
            predicates.add(criteriaBuilder.lessThan(root.get("price"), price_max+0.01));
        }
        predicates.add(criteriaBuilder.isTrue(root.get("active")));
        return predicates;
    }

    public void saveProduct(ProductFormDTO productFormDTO) throws ObjectDoesntExistException, RuntimeException {
        ProductEntity product = null;
        try{
            product = productMapper.createProductEntityFromProductFormDTO(productFormDTO);

        } catch (ObjectDoesntExistException e){
            throw new ObjectDoesntExistException("This category doesn't exist");
        }
        productDao.save(product);

        Arrays.stream(product.getImageUrls()).forEach(this::activateImage);
    }

    private void activateImage(String image) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(FILE_SERVICE+"?uuid="+image))
                .method("PATCH",HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteProduct(String uuid) throws ObjectDoesntExistException {
        ProductEntity product = productDao.findByUuid(uuid).orElse(null);
        if (product == null) throw new ObjectDoesntExistException("This product doesn't exist");
        product.setActive(false);
        productDao.save(product);
        Arrays.stream(product.getImageUrls()).forEach(this::deleteImage);
    }

    private void deleteImage(String image) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(FILE_SERVICE+"/?uuid="+image);
    }
}
