package com.evcar.domain.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vehicle {


    @Id
    @Column(name = "vehicle_id", nullable = false, length = 20)

    private String vehicleId;

    @Column(name = "brand", nullable = false, length = 20)
    private String brand;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "vehicle_class", nullable = false, length = 30)
    private String vehicleClass;

    @Column(name = "vehicle_status", nullable = false, length = 10)
    @Builder.Default
    private String vehicleStatus = "ACTIVE";

    @Column(name = "price_basic", nullable = false)
    private Integer priceBasic;

    @Column(name = "price_premium", nullable = false)
    @Builder.Default
    private Integer pricePremium = 0;

    @Column(name = "driving_range", nullable = false)
    @Builder.Default
    private Integer drivingRange = 0;

    @Column(name = "fast_charging_time", nullable = false, length = 30)
    @Builder.Default
    private String fastChargingTime = "";

    @Column(name = "slow_charging_time", nullable = false, length = 30)
    @Builder.Default
    private String slowChargingTime = "";

    @Column(name = "charging_method", nullable = false, length = 30)
    @Builder.Default
    private String chargingMethod = "";

    @Column(name = "battery_capacity", nullable = false, precision = 5, scale = 1)
    @Builder.Default
    private BigDecimal batteryCapacity = BigDecimal.ZERO;

    @Column(name = "image_url", nullable = false, length = 255)
    @Builder.Default
    private String imageUrl = "";

    @Column(name = "catalog_url", nullable = false, length = 255)
    @Builder.Default
    private String catalogUrl = "";

    @Column(name = "vehicle_url", nullable = false, length = 255)
    @Builder.Default
    private String vehicleUrl = "";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.vehicleStatus == null || this.vehicleStatus.isBlank()) {
            this.vehicleStatus = "ACTIVE";
        }
        if (this.pricePremium == null) {
            this.pricePremium = 0;
        }
        if (this.drivingRange == null) {
            this.drivingRange = 0;
        }
        if (this.fastChargingTime == null) {
            this.fastChargingTime = "";
        }
        if (this.slowChargingTime == null) {
            this.slowChargingTime = "";
        }
        if (this.chargingMethod == null) {
            this.chargingMethod = "";
        }
        if (this.batteryCapacity == null) {
            this.batteryCapacity = BigDecimal.ZERO;
        }
        if (this.imageUrl == null) {
            this.imageUrl = "";
        }
        if (this.catalogUrl == null) {
            this.catalogUrl = "";
        }
        if (this.vehicleUrl == null) {
            this.vehicleUrl = "";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}