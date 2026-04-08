package com.evcar.dto.charging;

import lombok.*;

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
    private String chargerType;
    private String status;
}