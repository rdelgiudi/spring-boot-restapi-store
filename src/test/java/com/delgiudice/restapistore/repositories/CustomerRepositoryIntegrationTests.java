package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositoryIntegrationTests {

    CustomerRepository underTest;

    @Autowired
    public CustomerRepositoryIntegrationTests(CustomerRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatCustomerCanBeCreatedAndRecalled() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(addressEntityA);
        underTest.save(customerEntityA);

        Optional<CustomerEntity> result = underTest.findById(customerEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(customerEntityA);
    }

    @Test
    public void testThatMultipleCustomersCanBeCreatedAndRecalled() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(addressEntityA);
        underTest.save(customerEntityA);

        AddressEntity addressEntityB = TestDataUtil.generateTestAddressEntityB();
        CustomerEntity customerEntityB = TestDataUtil.generateTestCustomerEntityB(addressEntityB);
        underTest.save(customerEntityB);

        AddressEntity addressEntityC = TestDataUtil.generateTestAddressEntityC();
        CustomerEntity customerEntityC = TestDataUtil.generateTestCustomerEntityC(addressEntityC);
        underTest.save(customerEntityC);

        Iterable<CustomerEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .contains(customerEntityA, customerEntityB, customerEntityC);
    }

    @Test
    public void testThatCustomerCanBeUpdated() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(addressEntityA);
        underTest.save(customerEntityA);

        customerEntityA.setEmail("updated@update.update");
        underTest.save(customerEntityA);

        Optional<CustomerEntity> result = underTest.findById(customerEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(customerEntityA);
    }

    @Test
    public void testThatCustomerCanBeDeleted() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(addressEntityA);
        underTest.save(customerEntityA);
        underTest.delete(customerEntityA);
        Optional<CustomerEntity> result = underTest.findById(customerEntityA.getId());
        assertThat(result).isEmpty();
    }
}
