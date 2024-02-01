package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.domain.dto.ProductDto;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import com.delgiudice.restapistore.mappers.Mapper;
import com.delgiudice.restapistore.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Presentation layer implementation
// This implementation allows for basic CRUD interaction with database via HTTP requests

@RestController
public class ProductController {

    private ProductService productService;

    private Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping(path = "/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity savedProductEntity = productService.create(productEntity);
        return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/products")
    public Page<ProductDto> listProducts(Pageable pageable) {
        Page<ProductEntity> productEntities = productService.findAll(pageable);
        return productEntities.map(productMapper::mapTo);
    }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Optional<ProductEntity> foundProduct = productService.findById(id);

        return foundProduct.map(productEntity -> {
            ProductDto productDto = productMapper.mapTo(productEntity);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/products/{id}")
    public ResponseEntity<ProductDto> fullUpdateProduct(@PathVariable Long id,
                                                        @RequestBody ProductDto productDto) {
        if (!productService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity updatedEntity = productService.update(id, productEntity);
        return new ResponseEntity<>(productMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/products/{id}")
    public ResponseEntity<ProductDto> partialProductUpdate(@PathVariable Long id,
                                                           @RequestBody ProductDto productDto) {
        if (!productService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity updatedEntity = productService.partialUpdate(id, productEntity);
        return new ResponseEntity<>(productMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
