package com.evcar.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminVehicleFormResponseDto {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private String vehicleStatus;
    private Integer priceBasic;
    private Integer pricePremium;
    private Integer drivingRange;
    private String fastChargingTime;
    private String slowChargingTime;
    private String chargingMethod;
    private BigDecimal batteryCapacity;
    private String imageUrl;
    private String catalogUrl;
    private String vehicleUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getCreatedAtFormatted() {
        if (createdAt == null) {
            return "-";
        }
        return createdAt.format(DATE_TIME_FORMATTER);
    }

    public String getUpdatedAtFormatted() {
        if (updatedAt == null) {
            return "-";
        }
        return updatedAt.format(DATE_TIME_FORMATTER);
    }
}