package com.evcar.dto.chatbot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VehicleChatbotDto {

    private String vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;
    private Integer priceBasic;
    private Integer pricePremium;
    private Integer drivingRange;
    private String fastChargingTime;
    private String slowChargingTime;
    private String chargingMethod;
    private Double batteryCapacity;
    private String imageUrl;
    private String catalogUrl;
    private String vehicleUrl;
}