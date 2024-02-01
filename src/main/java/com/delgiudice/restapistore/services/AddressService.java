package com.delgiudice.restapistore.services;

import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// Service layer interface

public interface AddressService {
    AddressEntity create(AddressEntity addressEntity);

    Page<AddressEntity> findAll(Pageable pageable);

    Optional<AddressEntity> findById(Long id);

    boolean existsById(Long id);

    AddressEntity update(Long id, AddressEntity addressEntity);

    AddressEntity partialUpdate(Long id, AddressEntity addressEntity);

    void deleteById(Long id);
}
