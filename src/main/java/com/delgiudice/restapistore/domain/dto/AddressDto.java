package com.delgiudice.restapistore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// A DTO (Data Transfer Object) is used to decouple the service and persistence layer from the presentation layer
// The presentation layer doesn't need to know the fields' additional properties, so no annotations are used for the
// fields

// Lombok annotations for automatically generating constructors, builders, toString(), hashCode() and equals()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    private String street;

    private Integer homeNumber;

    private Integer apartmentNumber;

    private String city;

    private String zipCode;


}
