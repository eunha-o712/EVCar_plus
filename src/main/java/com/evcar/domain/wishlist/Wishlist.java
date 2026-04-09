package com.evcar.domain.wishlist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Getter

public @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@Builder
public class Wishlist {

    @Id
    @Column(name = "wishlist_id", nullable = false, length = 20)
    private String wishlistId;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "vehicle_id", nullable = false, length = 20)
    private String vehicleId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}