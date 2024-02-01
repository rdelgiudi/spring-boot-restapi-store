package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.dto.ProductDto;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;
import com.delgiudice.restapistore.services.ProductService;
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
public class ProductControllerIntegrationTests {

    private ProductService productService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public ProductControllerIntegrationTests(ProductService productService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.productService = productService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateProductSuccessfullyReturnsStatus201Created() throws Exception{
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        productDto.setId(null);
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatedProductSuccessfullyReturnsSavedProduct() throws Exception {
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        productDto.setId(null);
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(productDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(productDto.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.inStock").value(productDto.getInStock())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(productDto.getPrice())
        );
    }

    @Test
    public void testThatListProductsReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsReturnsProduct() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        productService.create(productEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value(productEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].category").value(productEntity.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].inStock").value(productEntity.getInStock())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].price").value(productEntity.getPrice())
        );
    }

    @Test
    public void testThatGetProductReturnsStatus200OkWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetProductReturnsStatus404NotFoundWhenProductDoesNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetProductReturnsExpectedProductWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(productEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(productEntity.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.inStock").value(productEntity.getInStock())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(productEntity.getPrice())
        );
    }

    @Test
    public void testThatFullUpdateProductReturnsHttpStatus200OkWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateProductReturnsHttpStatus404NotFoundWhenProductDoesNotExist() throws Exception {
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateProductReturnsUpdatedProductWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        ProductDto productDto = TestDataUtil.generateTestProductDtoB();
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(productDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(productDto.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.inStock").value(productDto.getInStock())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(productDto.getPrice())
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsHttpStatus200OkWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsHttpStatus404NotFoundWhenProductDoesNotExist() throws Exception {
        ProductDto productDto = TestDataUtil.generateTestProductDtoA();
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsUpdatedProductWhenProductExists() throws Exception {
        ProductEntity productEntity = TestDataUtil.generateTestProductEntityA();
        ProductEntity savedProduct = productService.create(productEntity);

        ProductDto productDto = TestDataUtil.generateTestProductDtoNull();
        productDto.setCategory("Updated");
        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedProduct.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.category").value(productDto.getCategory())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.inStock").value(savedProduct.getInStock())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(savedProduct.getPrice())
        );
    }

    @Test
    public void testThatDeleteProductReturnsStatus204NoContent() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
