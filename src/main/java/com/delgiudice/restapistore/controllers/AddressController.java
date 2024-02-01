package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.domain.dto.AddressDto;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.mappers.Mapper;
import com.delgiudice.restapistore.services.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Presentation layer implementation
// This implementation allows for basic CRUD interaction with database via HTTP requests

@RestController
public class AddressController {

    private AddressService addressService;

    private Mapper<AddressEntity, AddressDto> addressMapper;

    public AddressController(AddressService addressService, Mapper<AddressEntity, AddressDto> addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping(path = "/addresses")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
        AddressEntity addressEntity = addressMapper.mapFrom(addressDto);
        AddressEntity savedAddressEntity = addressService.create(addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/addresses")
    public Page<AddressDto> listAddresses(Pageable pageable) {
        Page<AddressEntity> addressEntities = addressService.findAll(pageable);
        return addressEntities.map(addressMapper::mapTo);
    }

    @GetMapping(path = "/addresses/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable("id") Long id) {
        Optional<AddressEntity> foundAddress = addressService.findById(id);

        return foundAddress.map(addressEntity -> {
            AddressDto addressDto = addressMapper.mapTo(addressEntity);
            return new ResponseEntity<>(addressDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/addresses/{id}")
    public ResponseEntity<AddressDto> fullUpdateAddress(@PathVariable("id") Long id,
                                                        @RequestBody AddressDto addressDto) {
        if (!addressService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AddressEntity addressEntity = addressMapper.mapFrom(addressDto);
        AddressEntity savedAddressEntity = addressService.update(id, addressEntity);

        return new ResponseEntity<>(addressMapper.mapTo(savedAddressEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/addresses/{id}")
    public ResponseEntity<AddressDto> partialUpdateAddress(@PathVariable("id") Long id,
                                                           @RequestBody AddressDto addressDto) {

        if (!addressService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AddressEntity addressEntity = addressMapper.mapFrom(addressDto);
        AddressEntity updatedAddress = addressService.partialUpdate(id, addressEntity);
        return new ResponseEntity<>(addressMapper.mapTo(updatedAddress), HttpStatus.OK);
    }

    @DeleteMapping(path = "/addresses/{id}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long id) {
        addressService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
