package com.delgiudice.restapistore;

import com.delgiudice.restapistore.domain.dto.AddressDto;
import com.delgiudice.restapistore.domain.dto.CustomerDto;
import com.delgiudice.restapistore.domain.dto.OrderDto;
import com.delgiudice.restapistore.domain.dto.ProductDto;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;

public class TestDataUtil {

    public static AddressEntity generateTestAddressEntityA() {
        return AddressEntity.builder()
                .id(1L)
                .street("Oak Street")
                .homeNumber(1234)
                .apartmentNumber(56)
                .city("Springfield")
                .zipCode("12345")
                .build();
    }

    public static AddressDto generateTestAddressDtoA() {
        return AddressDto.builder()
                .id(1L)
                .street("Oak Street")
                .homeNumber(1234)
                .apartmentNumber(56)
                .city("Springfield")
                .zipCode("12345")
                .build();
    }

    public static AddressEntity generateTestAddressEntityB() {
        return AddressEntity.builder()
                .id(2L)
                .street("Maple Avenue")
                .homeNumber(5678)
                .apartmentNumber(789)
                .city("Lakeside City")
                .zipCode("67890")
                .build();
    }

    public static AddressDto generateTestAddressDtoB() {
        return AddressDto.builder()
                .id(2L)
                .street("Maple Avenue")
                .homeNumber(5678)
                .apartmentNumber(789)
                .city("Lakeside City")
                .zipCode("67890")
                .build();
    }

    public static AddressEntity generateTestAddressEntityC() {
        return AddressEntity.builder()
                .id(3L)
                .street("Elm Lane")
                .homeNumber(4321)
                .apartmentNumber(21)
                .city("Pineville")
                .zipCode("34567")
                .build();
    }

    public static AddressDto generateTestAddressDtoC() {
        return AddressDto.builder()
                .id(3L)
                .street("Elm Lane")
                .homeNumber(4321)
                .apartmentNumber(21)
                .city("Pineville")
                .zipCode("34567")
                .build();
    }

    public static AddressDto generateTestAddressDtoNull() {
        return AddressDto.builder().build();
    }

    public static CustomerEntity generateTestCustomerEntityA(AddressEntity addressEntity) {
        return CustomerEntity.builder()
                .id(1L)
                .name("Alice")
                .surname("Johnson")
                .email("alice.johnson@fake.co")
                .address(addressEntity)
                .build();
    }

    public static CustomerDto generateTestCustomerDtoA(AddressDto addressDto) {
        return CustomerDto.builder()
                .id(1L)
                .name("Alice")
                .surname("Johnson")
                .email("alice.johnson@fake.co")
                .address(addressDto)
                .build();
    }

    public static CustomerEntity generateTestCustomerEntityB(AddressEntity addressEntity) {
        return CustomerEntity.builder()
                .id(2L)
                .name("Brian")
                .surname("Smith")
                .email("brian.smith@fake.co")
                .address(addressEntity)
                .build();
    }

    public static CustomerDto generateTestCustomerDtoB(AddressDto addressDto) {
        return CustomerDto.builder()
                .id(2L)
                .name("Brian")
                .surname("Smith")
                .email("brian.smith@fake.co")
                .address(addressDto)
                .build();
    }

    public static CustomerEntity generateTestCustomerEntityC(AddressEntity addressEntity) {
        return CustomerEntity.builder()
                .id(3L)
                .name("Emily")
                .surname("Taylor")
                .email("emily.taylor@fake.co")
                .address(addressEntity)
                .build();
    }

    public static CustomerDto generateTestCustomerDtoC(AddressDto addressDto) {
        return CustomerDto.builder()
                .id(3L)
                .name("Emily")
                .surname("Taylor")
                .email("emily.taylor@fake.co")
                .address(addressDto)
                .build();
    }

    public static CustomerDto generateTestCustomerDtoNull() {
        return CustomerDto.builder().build();
    }

    public static ProductEntity generateTestProductEntityA() {
        return ProductEntity.builder()
                .id(1L)
                .name("Laptop Pro X1")
                .category("Electronics")
                .price(899f)
                .inStock(50)
                .build();
    }

    public static ProductDto generateTestProductDtoA() {
        return ProductDto.builder()
                .id(1L)
                .name("Laptop Pro X1")
                .category("Electronics")
                .price(899f)
                .inStock(50)
                .build();
    }

    public static ProductEntity generateTestProductEntityB() {
        return ProductEntity.builder()
                .id(2L)
                .name("Organic Coffee Beans")
                .category("Grocery")
                .price(12.99f)
                .inStock(200)
                .build();
    }

    public static ProductDto generateTestProductDtoB() {
        return ProductDto.builder()
                .id(2L)
                .name("Organic Coffee Beans")
                .category("Grocery")
                .price(12.99f)
                .inStock(200)
                .build();
    }

    public static ProductEntity generateTestProductEntityC() {
        return ProductEntity.builder()
                .id(3L)
                .name("Sports Bluetooth Headphones")
                .category("Electronics")
                .price(49.95f)
                .inStock(100)
                .build();
    }

    public static ProductDto generateTestProductDtoC() {
        return ProductDto.builder()
                .id(3L)
                .name("Sports Bluetooth Headphones")
                .category("Electronics")
                .price(49.95f)
                .inStock(100)
                .build();
    }

    public static ProductDto generateTestProductDtoNull() {
        return ProductDto.builder().build();
    }

    public static OrderEntity generateTestOrderEntity(CustomerEntity customer, ProductEntity product, int amount) {
        return OrderEntity.builder()
                .customer(customer)
                .product(product)
                .amount(amount)
                .build();
    }

    public static OrderDto generateTestOrderDto(CustomerDto customer, ProductDto product, int amount) {
        return OrderDto.builder()
                .customer(customer)
                .product(product)
                .amount(amount)
                .build();
    }
}
