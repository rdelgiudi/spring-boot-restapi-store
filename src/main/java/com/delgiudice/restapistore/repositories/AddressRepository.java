package com.delgiudice.restapistore.repositories;

import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>,
        PagingAndSortingRepository<AddressEntity, Long> {

}
