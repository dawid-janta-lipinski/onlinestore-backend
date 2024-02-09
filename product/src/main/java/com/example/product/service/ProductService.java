package com.example.product.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import com.example.product.dao.CategoryDao;
import com.example.product.dao.ProductDao;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

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
            categoryDao.findByShortId(category)
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

//    public void addProduct(ProductDTO product) {
//
//        categoryService.addCategory(product.getCategoryDTO());
//        productDao.save(productMapper.createProductEntityFromProductDTO(product));
//    }
}
