package com.delgiudice.restapistore.services;

import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    ProductEntity create(ProductEntity productEntity);

    Page<ProductEntity> findAll(Pageable pageable);

    Optional<ProductEntity> findById(Long id);

    boolean existsById(Long id);

    ProductEntity update(Long id, ProductEntity productEntity);

    ProductEntity partialUpdate(Long id, ProductEntity productEntity);

    void deleteById(Long id);
}
