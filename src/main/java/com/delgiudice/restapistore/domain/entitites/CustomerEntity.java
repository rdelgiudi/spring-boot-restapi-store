package com.delgiudice.restapistore.domain.entitites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.List;

// JPA Entity - Java object representation of database table

// Lombok annotations for automatically generating constructors, builders, toString(), hashCode() and equals()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// ****************
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // one-to-one relation (one address for one customer)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
