package com.delgiudice.restapistore.domain.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.ReadOnlyProperty;

// JPA Entity - Java object representation of database table

// Lombok annotations for automatically generating constructors, builders, toString(), hashCode() and equals()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// ****************
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // many-to-one relation (many orders to one customer)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // If customer stops existing, deletes all orders associated
    private CustomerEntity customer;

    @ManyToOne // many-to-one relation (many orders to one product)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // If product stops existing, deletes all orders associated
    private ProductEntity product;

    private Integer amount;

    @ReadOnlyProperty // this should only change when changing product in order
    private Float originalProductPrice;

    @ReadOnlyProperty // this should be calculated by the logic
    private Float total;
}
