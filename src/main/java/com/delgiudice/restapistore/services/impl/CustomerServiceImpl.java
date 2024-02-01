package com.delgiudice.restapistore.services.impl;

import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.repositories.CustomerRepository;
import com.delgiudice.restapistore.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Service layer implementation

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerEntity create(CustomerEntity customerEntity) {
        customerEntity.setId(null);
        return customerRepository.save(customerEntity);
    }

    @Override
    public Page<CustomerEntity> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Optional<CustomerEntity> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public CustomerEntity update(Long id, CustomerEntity customerEntity) {
        customerEntity.setId(id);
        return customerRepository.save(customerEntity);
    }

    @Override
    public CustomerEntity partialUpdate(Long id, CustomerEntity customerEntity) {
        customerEntity.setId(id);

        return customerRepository.findById(id).map(existingCustomer -> {
            Optional.ofNullable(customerEntity.getName()).ifPresent(existingCustomer::setName);
            Optional.ofNullable(customerEntity.getSurname()).ifPresent(existingCustomer::setSurname);
            Optional.ofNullable(customerEntity.getEmail()).ifPresent(existingCustomer::setEmail);
            Optional.ofNullable(customerEntity.getAddress()).ifPresent(existingCustomer::setAddress);   // This can also be removed if we want to update an address only from the AddressService
            return customerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer doesn't exists in database")); // Highly unlikely, unless presentation layer implementation changes
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
