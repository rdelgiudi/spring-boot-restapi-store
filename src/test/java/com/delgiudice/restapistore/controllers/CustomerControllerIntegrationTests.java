package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.dto.CustomerDto;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CustomerControllerIntegrationTests {

    private CustomerService customerService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public CustomerControllerIntegrationTests(CustomerService customerService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateCustomerSuccessfullyReturnsStatus201Created() throws Exception {
        CustomerDto customerDtoA = TestDataUtil.generateTestCustomerDtoA(null);
        String addressJson = objectMapper.writeValueAsString(customerDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateCustomerSuccessfullyReturnsCreatedCustomer() throws Exception {
        CustomerDto customerDtoA = TestDataUtil.generateTestCustomerDtoA(null);
        String addressJson = objectMapper.writeValueAsString(customerDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(customerDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.surname").value(customerDtoA.getSurname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(customerDtoA.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(customerDtoA.getAddress())
        );
    }

    @Test
    public void testThatListCustomersReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListCustomersReturnsCustomer() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedEntity = customerService.create(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(savedEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value(customerEntityA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].surname").value(customerEntityA.getSurname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].email").value(customerEntityA.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].address").value(customerEntityA.getAddress())
        );
    }

    @Test
    public void testThatGetCustomerReturnsHttpStatus200OkWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetCustomerReturnsHttpStatus404NotFoundWhenCustomerDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetCustomerReturnsExpectedCustomerWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedCustomerEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedCustomerEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.surname").value(savedCustomerEntity.getSurname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedCustomerEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(savedCustomerEntity.getAddress())
        );
    }

    @Test
    public void testThatFullUpdateCustomerReturnsHttpStatus200OkWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoA(null);
        String customerJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateCustomerReturnsHttpStatus404NotFoundWhenCustomerDoesNotExist() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        String customerJson = objectMapper.writeValueAsString(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateCustomerReturnsUpdatedCustomerWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoB(null);
        String customerJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedCustomerEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(customerDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.surname").value(customerDto.getSurname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(customerDto.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(customerDto.getAddress())
        );
    }

    @Test
    public void testThatPartialUpdateCustomerReturnsHttpStatus200OkWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoNull();
        String customerJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateCustomerReturnsHttpStatus404NotFoundWhenCustomerDoesNotExist() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        String customerJson = objectMapper.writeValueAsString(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/customers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateCustomerReturnsUpdatedCustomerWhenCustomerExists() throws Exception {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        CustomerEntity savedCustomerEntity = customerService.create(customerEntityA);

        CustomerDto customerDto = TestDataUtil.generateTestCustomerDtoNull();
        customerDto.setEmail("update@updated.co");
        String customerJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/customers/" + savedCustomerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedCustomerEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedCustomerEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.surname").value(savedCustomerEntity.getSurname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(customerDto.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(savedCustomerEntity.getAddress())
        );
    }

    @Test
    public void testThatDeleteCustomerReturnsStatus400NoContent() throws Exception{
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        String customerJson = objectMapper.writeValueAsString(customerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
