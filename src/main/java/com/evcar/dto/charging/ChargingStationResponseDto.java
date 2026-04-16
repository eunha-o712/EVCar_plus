package com.evcar.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargingStationResponseDto {

    private String stationId;
    private String stationName;
    private String address;
    private double lat;
    private double lng;
    private String operatorName;
    private String operatorCall;
    private String useTime;
    private String parkingFree;
    private String note;
}