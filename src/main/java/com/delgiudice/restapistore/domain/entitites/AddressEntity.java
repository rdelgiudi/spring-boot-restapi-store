package com.delgiudice.restapistore.domain.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA Entity - Java object representation of database table

// Lombok annotations for automatically generating constructors, builders, toString(), hashCode() and equals()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// ****************
@Entity
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private Integer homeNumber;

    private Integer apartmentNumber;

    private String city;

    private String zipCode;
}
