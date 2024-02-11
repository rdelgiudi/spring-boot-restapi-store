package com.delgiudice.restapistore.services.impl;

import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import com.delgiudice.restapistore.repositories.OrderRepository;
import com.delgiudice.restapistore.services.CustomerService;
import com.delgiudice.restapistore.services.OrderService;
import com.delgiudice.restapistore.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Service layer implementation

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    ProductService productService;

    CustomerService customerService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    private void modifyProductsInStock(OrderEntity orderEntity, int amount) {
        ProductEntity product = ProductEntity.builder().inStock(amount).build();
        productService.partialUpdate(orderEntity.getProduct().getId(), product);
    }

    private static void recalculateOrderPrice(OrderEntity orderEntity) {
        if (orderEntity.getProduct() != null) {
            orderEntity.setTotal(orderEntity.getOriginalProductPrice() * orderEntity.getAmount());
        }
    }
    private static void calculateOrderPrice(OrderEntity orderEntity) {
        if (orderEntity.getProduct() != null && orderEntity.getAmount() != null) {
            float originalPrice = orderEntity.getProduct().getPrice();
            orderEntity.setOriginalProductPrice(originalPrice);
            orderEntity.setTotal(originalPrice * orderEntity.getAmount());
        }
    }

    @Override
    public OrderEntity create(OrderEntity orderEntity) {
        calculateOrderPrice(orderEntity);

//        if (orderEntity.getProduct().getInStock() != null && orderEntity.getAmount() != null) {
//            int newAmountInStock = orderEntity.getProduct().getInStock() - orderEntity.getAmount();
//            modifyProductsInStock(orderEntity, newAmountInStock);
//        }
        orderEntity.setId(null);

        return orderRepository.save(orderEntity);
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderEntity> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public OrderEntity update(Long id, OrderEntity orderEntity) {
        orderEntity.setId(id);
        calculateOrderPrice(orderEntity);
        // Should the product amount from the previous order be returned to stock if order fully updated?
//        if (orderEntity.getProduct().getInStock() != null && orderEntity.getAmount() != null) {
//            int newAmountInStock = orderEntity.getProduct().getInStock() - orderEntity.getAmount();
//            modifyProductsInStock(orderEntity, newAmountInStock);
//        }

        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity partialUpdate(Long id, OrderEntity orderEntity) {
        orderEntity.setId(id);

        // Once an order is created in the database, it should not be possible to modify
        // any fields expect the amount, unless the whole order is updated
        return orderRepository.findById(id).map(existingOrder -> {
            int oldAmount = existingOrder.getAmount();
            Optional.ofNullable(orderEntity.getAmount()).ifPresent(amount -> {
                existingOrder.setAmount(amount);
//                if (existingOrder.getProduct().getInStock() != null) {
//                    int diff = oldAmount - existingOrder.getAmount();
//                    if (diff != 0) {
//                        int newAmountInStock = orderEntity.getProduct().getInStock() + diff;
//                        modifyProductsInStock(orderEntity, newAmountInStock);
//                    }
//                }
            });

            recalculateOrderPrice(existingOrder);
            return existingOrder;
        }).orElseThrow(() -> new RuntimeException("Order does not exist")); // Highly unlikely, unless presentation layer implementation changes
    }

    @Override
    public void deleteById(Long id) {
        // Should the product amount from the previous order be returned to stock if order deleted?
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderEntity> findByCustomerId(Long customerId) {
        Iterable<OrderEntity> foundOrder = orderRepository.findByCustomerIdEquals(customerId);
        return StreamSupport.stream(foundOrder.spliterator(), false)
                .collect(Collectors.toList());
        //or
        // List<OrderEntity> resultList = new ArrayList<>();
        // foundOrder.forEach(resultList::add);
        // return resultList;
    }

    @Override
    public Optional<OrderEntity> createWithProductAndCustomerId(OrderEntity orderEntity, Long customerId, Long productId) {
        Optional<CustomerEntity> customerEntity = customerService.findById(customerId);
        if (customerEntity.isPresent()) {
            orderEntity.setCustomer(customerEntity.get());
        }
        else {
            return Optional.empty();
        }

        Optional<ProductEntity> productEntity = productService.findById(productId);
        if (productEntity.isPresent()) {
            orderEntity.setProduct(productEntity.get());
        }
        else {
            return Optional.empty();
        }

        orderEntity.setId(null);
        return Optional.of(orderRepository.save(orderEntity));
    }
}
