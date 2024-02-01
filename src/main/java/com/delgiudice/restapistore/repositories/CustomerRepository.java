package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.domain.entitites.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> ,
        PagingAndSortingRepository<CustomerEntity, Long> {
}
