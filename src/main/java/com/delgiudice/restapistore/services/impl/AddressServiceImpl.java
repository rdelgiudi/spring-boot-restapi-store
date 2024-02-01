package com.delgiudice.restapistore.services.impl;

import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.repositories.AddressRepository;
import com.delgiudice.restapistore.services.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Service layer implementation

@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressEntity create(AddressEntity addressEntity) {
        addressEntity.setId(null);
        return addressRepository.save(addressEntity);
    }

    @Override
    public Page<AddressEntity> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Optional<AddressEntity> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return addressRepository.existsById(id);
    }

    @Override
    public AddressEntity update(Long id, AddressEntity addressEntity) {
        addressEntity.setId(id);
        return addressRepository.save(addressEntity);
    }

    @Override
    public AddressEntity partialUpdate(Long id, AddressEntity addressEntity) {
        addressEntity.setId(id);

        return addressRepository.findById(id).map(existingAddress -> {
            Optional.ofNullable(addressEntity.getStreet()).ifPresent(existingAddress::setStreet);
            Optional.ofNullable(addressEntity.getHomeNumber()).ifPresent(existingAddress::setHomeNumber);
            Optional.ofNullable(addressEntity.getApartmentNumber()).ifPresent(existingAddress::setApartmentNumber);
            Optional.ofNullable(addressEntity.getCity()).ifPresent(existingAddress::setCity);
            Optional.ofNullable(addressEntity.getZipCode()).ifPresent(existingAddress::setZipCode);
            return addressRepository.save(existingAddress);
        }).orElseThrow(() -> new RuntimeException("Address doesn't exist in database")); // Highly unlikely, unless presentation layer implementation changes
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }
}
