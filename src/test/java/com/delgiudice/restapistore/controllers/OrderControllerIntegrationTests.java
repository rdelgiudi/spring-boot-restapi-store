package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.dto.CustomerDto;
import com.delgiudice.restapistore.domain.dto.OrderDto;
import com.delgiudice.restapistore.domain.dto.ProductDto;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import com.delgiudice.restapistore.services.CustomerService;
import com.delgiudice.restapistore.services.OrderService;
import com.delgiudice.restapistore.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests {

    private OrderService orderService;

    private CustomerService customerService;

    private ProductService productService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public OrderControllerIntegrationTests(OrderService orderService,
                                           CustomerService customerService,
                                           ProductService productService,
                                           MockMvc mockMvc,
                                           ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    public void testThatCreateOrderSuccessfullyReturnsHttp201Created() throws Exception{
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateOrderSuccessfullyReturnsCreatedOrder() throws Exception{
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customer").value(orderDto.getCustomer())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product").value(orderDto.getProduct())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.amount").value(orderDto.getAmount())
        );
    }

    @Test
    public void testThatListOrdersSuccessfullyReturnsStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListOrdersSuccessfullyReturnsOrderList() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        float calculatedTotal = orderEntity.getAmount() * orderEntity.getProduct().getPrice();
        float originalProductPrice = orderEntity.getProduct().getPrice();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(savedEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].customer").value(savedEntity.getCustomer())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].product").value(savedEntity.getProduct())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].amount").value(savedEntity.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].originalProductPrice").value(originalProductPrice)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].total").value(calculatedTotal)
        );
    }

    @Test
    public void testThatGetOrderReturnsHttpStatus200OkWhenOrderExists() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetOrderReturnsHttpStatus404NotFoundWhenOrderDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetOrderReturnsExpectedOrderWhenOrderExists() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        float calculatedTotal = orderEntity.getAmount() * orderEntity.getProduct().getPrice();
        float originalProductPrice = orderEntity.getProduct().getPrice();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customer").value(savedEntity.getCustomer())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product").value(savedEntity.getProduct())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.amount").value(savedEntity.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.originalProductPrice").value(originalProductPrice)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total").value(calculatedTotal)
        );
    }

    @Test
    public void testThatFullUpdateOrderReturnsHttpStatus200OkWhenOrderExists() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateOrderReturnsHttpStatus404NotFoundWhenOrderDoesNotExist() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateOrderReturnsUpdatedOrderWhenOrderExists() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 10);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        float calculatedTotal = orderDto.getAmount() * orderDto.getProduct().getPrice();
        float originalProductPrice = orderDto.getProduct().getPrice();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customer").value(orderDto.getCustomer())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product").value(orderDto.getProduct())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.amount").value(orderDto.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.originalProductPrice").value(originalProductPrice)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total").value(calculatedTotal)
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsStatus200OkWhenOrderExists() throws Exception{
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsHttpStatus404NotFoundWhenOrderDoesNotExist() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(customerDto, productDto, 5);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsUpdatedOrderWhenOrderExists() throws Exception {
        CustomerEntity customer = TestDataUtil.generateTestCustomerEntityA(null);
        customerService.create(customer);
        ProductEntity product = TestDataUtil.generateTestProductEntityA();
        productService.create(product);

        OrderEntity orderEntity = TestDataUtil.generateTestOrderEntity(customer, product, 5);
        OrderEntity savedEntity = orderService.create(orderEntity);

        OrderDto orderDto = TestDataUtil.generateTestOrderDto(null, null, 10);
        String orderJson = objectMapper.writeValueAsString(orderDto);

        float calculatedTotal = orderDto.getAmount() * savedEntity.getProduct().getPrice();
        float originalProductPrice = savedEntity.getProduct().getPrice();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customer").value(savedEntity.getCustomer())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product").value(savedEntity.getProduct())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.amount").value(orderDto.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.originalProductPrice").value(originalProductPrice)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total").value(calculatedTotal)
        );
    }

    @Test
    public void testThatDeleteOrderReturnsStatus204NoContent() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
