package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long>,
        PagingAndSortingRepository<ProductEntity, Long> {

    @Query("SELECT a FROM ProductEntity a WHERE a.inStock = 0")
    Iterable<ProductEntity> findAllProductsOutOfStock();

    @Query("SELECT a FROM ProductEntity a WHERE a.inStock > 0")
    Iterable<ProductEntity> findAllProductsInStock();

    @Query("SELECT a FROM ProductEntity a WHERE a.inStock > 0 AND a.inStock < ?1")
    Iterable<ProductEntity> findAllProductsLowStock(int threshold);
}
