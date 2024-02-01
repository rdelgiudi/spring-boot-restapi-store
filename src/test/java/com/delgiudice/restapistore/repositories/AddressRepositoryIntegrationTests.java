package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.TestDataUtil;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
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
public class AddressRepositoryIntegrationTests {

    private AddressRepository underTest;

    @Autowired
    public AddressRepositoryIntegrationTests(AddressRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAddressCanBeCreatedAndRecalled() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        underTest.save(addressEntityA);

        Optional<AddressEntity> result = underTest.findById(addressEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(addressEntityA);
    }

    @Test
    public void testThatMultipleAddressesCanBeCreatedAndRecalled() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        underTest.save(addressEntityA);

        AddressEntity addressEntityB = TestDataUtil.generateTestAddressEntityB();
        underTest.save(addressEntityB);

        AddressEntity addressEntityC = TestDataUtil.generateTestAddressEntityC();
        underTest.save(addressEntityC);

        Iterable<AddressEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .contains(addressEntityA, addressEntityB, addressEntityC);
    }

    @Test
    public void testThatAddressCanBeUpdated() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        underTest.save(addressEntityA);

        addressEntityA.setCity("Updateville");

        underTest.save(addressEntityA);

        Optional<AddressEntity> result = underTest.findById(addressEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(addressEntityA);
    }

    @Test
    public void testThatAddressCanBeDeleted() {
        AddressEntity addressEntityA = TestDataUtil.generateTestAddressEntityA();
        underTest.save(addressEntityA);
        underTest.deleteById(addressEntityA.getId());

        Optional<AddressEntity> result = underTest.findById(addressEntityA.getId());
        assertThat(result).isEmpty();
    }
}
