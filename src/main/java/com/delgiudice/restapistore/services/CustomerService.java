package com.delgiudice.restapistore.services;

import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// Service layer interface

public interface CustomerService {

    CustomerEntity create(CustomerEntity customerEntity);

    Page<CustomerEntity> findAll(Pageable pageable);

    Optional<CustomerEntity> findById(Long id);

    boolean existsById(Long id);

    CustomerEntity update(Long id, CustomerEntity customerEntity);

    CustomerEntity partialUpdate(Long id, CustomerEntity customerEntity);

    void deleteById(Long id);
}
