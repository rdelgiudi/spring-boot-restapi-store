package com.delgiudice.restapistore.services.impl;

import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import com.delgiudice.restapistore.repositories.ProductRepository;
import com.delgiudice.restapistore.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Service layer implementation

@Service
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity create(ProductEntity productEntity) {
        productEntity.setId(null);
        return productRepository.save(productEntity);
    }

    @Override
    public Page<ProductEntity> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public ProductEntity update(Long id, ProductEntity productEntity) {
        productEntity.setId(id);
        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity partialUpdate(Long id, ProductEntity productEntity) {
        productEntity.setId(id);

        return productRepository.findById(id).map(existingProduct -> {
            Optional.ofNullable(productEntity.getName()).ifPresent(existingProduct::setName);
            Optional.ofNullable(productEntity.getCategory()).ifPresent(existingProduct::setCategory);
            Optional.ofNullable(productEntity.getInStock()).ifPresent(existingProduct::setInStock);
            Optional.ofNullable(productEntity.getPrice()).ifPresent(existingProduct::setPrice);
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product doesn't exist")); // Highly unlikely, unless presentation layer implementation changes
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
