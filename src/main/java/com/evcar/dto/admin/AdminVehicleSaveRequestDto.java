package com.evcar.dto.admin;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminVehicleSaveRequestDto {

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
}