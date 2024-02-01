package com.delgiudice.restapistore.controllers;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.dto.AddressDto;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.services.AddressService;
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
public class AddressControllerIntegrationTests {

    private AddressService addressService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AddressControllerIntegrationTests(AddressService addressService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.addressService = addressService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateAddressSuccessfullyReturnsHttp201Created() throws Exception {
        AddressDto addressDtoA = TestDataUtil.generateTestAddressDtoA();
        addressDtoA.setId(null);
        String addressJson = objectMapper.writeValueAsString(addressDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatedAddressSuccessfullyReturnsSavedAddress() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        addressEntityA.setId(null);
        String addressJson = objectMapper.writeValueAsString(addressEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value(addressEntityA.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.homeNumber").value(addressEntityA.getHomeNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.apartmentNumber").value(addressEntityA.getApartmentNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.city").value(addressEntityA.getCity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.zipCode").value(addressEntityA.getZipCode())
        );
    }

    @Test
    public void testThatListAddressesReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAddressesReturnsAddress() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        addressService.create(addressEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].street").value(addressEntityA.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].homeNumber").value(addressEntityA.getHomeNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].apartmentNumber").value(addressEntityA.getApartmentNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].city").value(addressEntityA.getCity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].zipCode").value(addressEntityA.getZipCode())
        );
    }

    @Test
    public void testThatGetAddressReturnsHttpStatus200WhenAddressExists() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAddressReturnsHttpStatus404WhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAddressReturnsExpectedAddressWhenAddressExists() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        addressService.create(addressEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/" + addressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(addressEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value(addressEntityA.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.homeNumber").value(addressEntityA.getHomeNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.apartmentNumber").value(addressEntityA.getApartmentNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.city").value(addressEntityA.getCity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.zipCode").value(addressEntityA.getZipCode())
        );
    }

    @Test
    public void testThatFullUpdateAddressReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        AddressDto addressDtoA = TestDataUtil.generateTestAddressDtoA();
        String addressJson = objectMapper.writeValueAsString(addressDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAddressReturnsHttpStatus404WhenAddressDoesNotExists() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        String addressJson = objectMapper.writeValueAsString(addressEntityA);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAddressUpdatesExistingAddress() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        AddressDto addressDtoB = TestDataUtil.generateTestAddressDtoB();
        String addressJson = objectMapper.writeValueAsString(addressDtoB);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAddressEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value(addressDtoB.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.homeNumber").value(addressDtoB.getHomeNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.apartmentNumber").value(addressDtoB.getApartmentNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.city").value(addressDtoB.getCity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.zipCode").value(addressDtoB.getZipCode())
        );
    }

    @Test
    public void testThatPartialUpdateAddressReturnStatus200OkWhenAddressExists() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        AddressDto addressDtoUpdate = TestDataUtil.generateTestAddressDtoNull();
        String addressJson = objectMapper.writeValueAsString(addressDtoUpdate);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateAddressReturnStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        AddressDto addressDtoUpdate = TestDataUtil.generateTestAddressDtoNull();
        String addressJson = objectMapper.writeValueAsString(addressDtoUpdate);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateAddressUpdatesExistingAddress() throws Exception {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        AddressDto addressDtoUpdate = TestDataUtil.generateTestAddressDtoNull();
        addressDtoUpdate.setStreet("Update Avenue");
        String addressJson = objectMapper.writeValueAsString(addressDtoUpdate);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAddressEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value(addressDtoUpdate.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.homeNumber").value(savedAddressEntityA.getHomeNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.apartmentNumber").value(savedAddressEntityA.getApartmentNumber())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.city").value(savedAddressEntityA.getCity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.zipCode").value(savedAddressEntityA.getZipCode())
        );
    }

    @Test
    public void testThatDeleteAddressReturnsStatus204NoContentForNonExistingAddress() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAddressReturnsStatus204NoContentForExistingAddress() throws Exception{
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        AddressEntity savedAddressEntityA = addressService.create(addressEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/addresses/" + savedAddressEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
