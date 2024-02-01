package com.delgiudice.restapistore.services;

import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

// Service layer interface

public interface OrderService {

    OrderEntity create(OrderEntity orderEntity);

    Page<OrderEntity> findAll(Pageable pageable);

    Optional<OrderEntity> findById(Long id);

    boolean existsById(Long id);

    OrderEntity update(Long id, OrderEntity orderEntity);

    OrderEntity partialUpdate(Long id, OrderEntity orderEntity);

    void deleteById(Long id);
}
