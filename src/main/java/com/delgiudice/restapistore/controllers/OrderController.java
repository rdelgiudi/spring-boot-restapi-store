package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.domain.dto.OrderDto;
import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import com.delgiudice.restapistore.mappers.Mapper;
import com.delgiudice.restapistore.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Presentation layer implementation
// This implementation allows for basic CRUD interaction with database via HTTP requests

@RestController
public class OrderController {

    private OrderService orderService;

    private Mapper<OrderEntity, OrderDto> orderMapper;

    public OrderController(OrderService orderService, Mapper<OrderEntity, OrderDto> orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping(path = "/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        OrderEntity savedOrderEntity = orderService.create(orderEntity);
        return new ResponseEntity<>(orderMapper.mapTo(savedOrderEntity), HttpStatus.CREATED);
    }

    @PostMapping(path = "/customers/{customer_id}/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestParam(name = "product_id") Long productId,
                                                @PathVariable("customer_id") Long customerId,
                                                @RequestBody OrderDto orderDto) {
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        Optional<OrderEntity> savedOrderEntity = orderService.createWithProductAndCustomerId(orderEntity,
                customerId, productId);

        return savedOrderEntity.map(savedOrder -> {
            OrderDto savedOrderDto = orderMapper.mapTo(savedOrder);
            return new ResponseEntity<>(savedOrderDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(path = "/orders")
    public Page<OrderDto> listOrders(Pageable pageable) {
        Page<OrderEntity> orderList = orderService.findAll(pageable);
        return orderList.map(orderMapper::mapTo);
    }

    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") Long id) {
        Optional<OrderEntity> foundOrder = orderService.findById(id);

        return foundOrder.map(orderEntity -> {
            OrderDto orderDto = orderMapper.mapTo(orderEntity);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/customers/{customer_id}/orders")
    public List<OrderDto> getCustomerOrders(@PathVariable("customer_id") Long customerId) {
        // This could first check if customer exists
        List<OrderEntity> foundOrders = orderService.findByCustomerId(customerId);

        return foundOrders.stream().map(orderMapper::mapTo).toList();
    }

    @PutMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDto> fullUpdateOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        if (!orderService.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        OrderEntity updatedEntity = orderService.update(id, orderEntity);
        return new ResponseEntity<>(orderMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDto> partialUpdateOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {

        if (!orderService.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        OrderEntity updatedEntity = orderService.partialUpdate(id, orderEntity);
        return new ResponseEntity<>(orderMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
