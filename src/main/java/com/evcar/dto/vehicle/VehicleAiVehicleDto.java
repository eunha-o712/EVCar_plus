package com.evcar.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAiVehicleDto {

    private String vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private Integer priceBasic;
    private Integer pricePremium;
    private Integer drivingRange;
    private Integer fastChargingTime;
    private Integer slowChargingTime;
    private String chargingMethod;
    private Double batteryCapacity;
}