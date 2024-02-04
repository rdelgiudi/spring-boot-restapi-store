package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long>,
        PagingAndSortingRepository<OrderEntity, Long> {

    //@Query("SELECT a FROM OrderEntity a WHERE a.total > ?1")
    Iterable<OrderEntity> findByTotalGreaterThan(float total);

    //@Query("SELECT a FROM OrderEntity a WHERE a.customer.id = ?1")
    Iterable<OrderEntity> findByCustomerIdEquals(long customerId);
}
