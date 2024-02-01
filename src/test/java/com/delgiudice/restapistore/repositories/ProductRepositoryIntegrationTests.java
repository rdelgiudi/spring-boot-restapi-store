package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.TestDataUtil;
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
public class ProductRepositoryIntegrationTests {

    ProductRepository underTest;

    @Autowired
    public ProductRepositoryIntegrationTests(ProductRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatProductCanBeCreatedAndRecalled() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        underTest.save(productEntityA);

        Optional<ProductEntity> result = underTest.findById(productEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntityA);
    }

    @Test
    public void testThatMultipleProductsCanBeCreatedAndRecalled() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        underTest.save(productEntityA);

        ProductEntity productEntityB = TestDataUtil.generateTestProductEntityB();
        underTest.save(productEntityB);

        ProductEntity productEntityC = TestDataUtil.generateTestProductEntityC();
        underTest.save(productEntityC);

        Iterable<ProductEntity> result = underTest.findAll();

        assertThat(result)
                .hasSize(3)
                .contains(productEntityA, productEntityB, productEntityC);
    }

    @Test
    public void testThatProductCanBeUpdated() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        underTest.save(productEntityA);
        productEntityA.setName("Update");
        underTest.save(productEntityA);

        Optional<ProductEntity> result = underTest.findById(productEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntityA);
    }

    @Test
    public void testThatProductCanBeDeleted() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        underTest.save(productEntityA);
        underTest.deleteById(productEntityA.getId());

        Optional<ProductEntity> result = underTest.findById(productEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatFindAllProductsOutOfStockAndInStockReturnsTheExpectedProducts() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productEntityA.setInStock(0);
        underTest.save(productEntityA);

        ProductEntity productEntityB = TestDataUtil.generateTestProductEntityB();
        underTest.save(productEntityB);

        ProductEntity productEntityC = TestDataUtil.generateTestProductEntityC();
        productEntityC.setInStock(0);
        underTest.save(productEntityC);

        Iterable<ProductEntity> resultOutOfStock = underTest.findAllProductsOutOfStock();
        assertThat(resultOutOfStock)
                .hasSize(2)
                .containsExactly(productEntityA, productEntityC);

        Iterable<ProductEntity> resultInStock = underTest.findAllProductsInStock();
        assertThat(resultInStock)
                .hasSize(1)
                .containsExactly(productEntityB);
    }

    @Test
    public void testThatFindAllProductsLowOnStockReturnsTheExpectedProducts() {
        ProductEntity productEntityA = TestDataUtil.generateTestProductEntityA();
        productEntityA.setInStock(2);
        underTest.save(productEntityA);

        ProductEntity productEntityB = TestDataUtil.generateTestProductEntityB();
        productEntityB.setInStock(3);
        underTest.save(productEntityB);

        ProductEntity productEntityC = TestDataUtil.generateTestProductEntityC();
        productEntityC.setInStock(10);
        underTest.save(productEntityC);

        Iterable<ProductEntity> result = underTest.findAllProductsLowStock(4);
        assertThat(result)
                .hasSize(2)
                .containsExactly(productEntityA, productEntityB);
    }
}
