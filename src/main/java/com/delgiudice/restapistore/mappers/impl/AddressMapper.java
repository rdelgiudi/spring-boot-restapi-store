package com.delgiudice.restapistore.mappers.impl;

import com.delgiudice.restapistore.domain.dto.AddressDto;
import com.delgiudice.restapistore.domain.entitites.AddressEntity;
import com.delgiudice.restapistore.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements Mapper<AddressEntity, AddressDto> {

    private ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDto mapTo(AddressEntity addressEntity) {
        return modelMapper.map(addressEntity, AddressDto.class);
    }

    @Override
    public AddressEntity mapFrom(AddressDto addressDto) {
        return modelMapper.map(addressDto, AddressEntity.class);
    }
}
