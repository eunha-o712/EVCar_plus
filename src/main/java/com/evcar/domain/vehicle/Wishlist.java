package com.evcar.domain.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "wishlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = "vehicle_id")
})
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    private LocalDateTime createdAt = LocalDateTime.now();
}