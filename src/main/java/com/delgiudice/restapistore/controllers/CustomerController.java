package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.domain.dto.CustomerDto;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.mappers.Mapper;
import com.delgiudice.restapistore.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


// Presentation layer implementation
// This implementation allows for basic CRUD interaction with database via HTTP requests

@RestController
public class CustomerController {

    private CustomerService customerService;

    private Mapper<CustomerEntity, CustomerDto> customerMapper;

    public CustomerController(CustomerService customerService, Mapper<CustomerEntity, CustomerDto> customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping(path = "/customers")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerEntity customerEntity = customerMapper.mapFrom(customerDto);
        CustomerEntity savedCustomer = customerService.create(customerEntity);
        return new ResponseEntity<>(customerMapper.mapTo(savedCustomer), HttpStatus.CREATED);
    }

    @GetMapping(path = "/customers")
    public Page<CustomerDto> listCustomers(Pageable pageable) {
        Page<CustomerEntity> customerEntities = customerService.findAll(pageable);
        return customerEntities.map(customerMapper::mapTo);
    }

    @GetMapping(path = "/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Long id) {
        Optional<CustomerEntity> foundCustomer = customerService.findById(id);

        return foundCustomer.map(customerEntity -> {
            CustomerDto customerDto = customerMapper.mapTo(customerEntity);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/customers/{id}")
    public ResponseEntity<CustomerDto> fullUpdateCustomer(@PathVariable("id") Long id,
                                                          @RequestBody CustomerDto customerDto) {
        if (!customerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CustomerEntity customerEntity = customerMapper.mapFrom(customerDto);
        CustomerEntity updatedCustomer = customerService.update(id, customerEntity);

        return new ResponseEntity<>(customerMapper.mapTo(updatedCustomer), HttpStatus.OK);
    }

    @PatchMapping(path = "/customers/{id}")
    public ResponseEntity<CustomerDto> partialUpdateAddress(@PathVariable("id") Long id,
                                                           @RequestBody CustomerDto customerDto) {
        if (!customerService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CustomerEntity customerEntity = customerMapper.mapFrom(customerDto);
        CustomerEntity updatedCustomer = customerService.partialUpdate(id, customerEntity);

        return new ResponseEntity<>(customerMapper.mapTo(updatedCustomer), HttpStatus.OK);
    }

    @DeleteMapping(path = "/customers/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long id) {
        customerService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
