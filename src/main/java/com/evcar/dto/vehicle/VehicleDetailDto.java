package com.evcar.dto.vehicle;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailDto {

    // 기본 정보
    private Long vehicleId;
    private String brand;
    private String modelName;
    private String vehicleClass;

    // 가격 (🔥 Integer로 변경)
    private Integer priceBasic;
    private Integer pricePremium;

    // 주행 / 충전 (🔥 이것도 안전하게 Integer 추천)
    private Integer drivingRange;
    private String fastChargingTime;
    private String slowChargingTime;
    private String chargingMethod;

    // 배터리 (🔥 이것도 Double로)
    private Double batteryCapacity;

    // 이미지 & 링크
    private String imageUrl;
    private String catalogUrl;

    // 위시리스트 여부
    private boolean wished = false;
}