package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import com.delgiudice.restapistore.domain.entitites.OrderEntity;
import com.delgiudice.restapistore.domain.entitites.ProductEntity;
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
public class OrderRepositoryIntegrationTests {

    OrderRepository underTest;
    ProductRepository productRepository;
    CustomerRepository customerRepository;

    @Autowired
    public OrderRepositoryIntegrationTests(OrderRepository underTest, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.underTest = underTest;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Test
    public void testThatOrderCanBeCreatedAndRecalled() {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        OrderEntity orderEntityA = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 5);
        productRepository.save(productEntityA);
        customerRepository.save(customerEntityA);
        underTest.save(orderEntityA);

        Optional<OrderEntity> result = underTest.findById(orderEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderEntityA);
    }

    @Test
    public void testThatMultipleOrdersCanBeCreatedAndRecalled() {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productRepository.save(productEntityA);
        customerRepository.save(customerEntityA);

        OrderEntity orderEntityA = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 1);
        OrderEntity orderEntityB = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 2);
        OrderEntity orderEntityC = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 3);

        underTest.save(orderEntityA);
        underTest.save(orderEntityB);
        underTest.save(orderEntityC);

        Iterable<OrderEntity> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .contains(orderEntityA, orderEntityB, orderEntityC);
    }

    @Test
    public void testThatOrderCanBeModified() {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productRepository.save(productEntityA);
        customerRepository.save(customerEntityA);

        OrderEntity orderEntityA = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 1);
        underTest.save(orderEntityA);
        orderEntityA.setAmount(200);
        underTest.save(orderEntityA);

        Optional<OrderEntity> result = underTest.findById(orderEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderEntityA);
    }

    @Test
    public void testThatOrderCanBeDeleted() {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productRepository.save(productEntityA);
        customerRepository.save(customerEntityA);

        OrderEntity orderEntityA = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 1);
        underTest.save(orderEntityA);
        underTest.deleteById(orderEntityA.getId());

        Optional<OrderEntity> result = underTest.findById(orderEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatTotalMoreThanReturnsTheExpectedOrders() {
        CustomerEntity customerEntityA = TestDataUtil.generateTestCustomerEntityA(null);
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productRepository.save(productEntityA);
        customerRepository.save(customerEntityA);

        OrderEntity orderEntityA = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 1);
        orderEntityA.setTotal(5f);
        underTest.save(orderEntityA);
        OrderEntity orderEntityB = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 2);
        orderEntityB.setTotal(10f);
        underTest.save(orderEntityB);
        OrderEntity orderEntityC = TestDataUtil.generateTestOrderEntity(customerEntityA, productEntityA, 3);
        orderEntityC.setTotal(15f);
        underTest.save(orderEntityC);

        Iterable<OrderEntity> result = underTest.findByTotalGreaterThan(7f);
        assertThat(result)
                .hasSize(2)
                .containsExactly(orderEntityB, orderEntityC);
    }
}
