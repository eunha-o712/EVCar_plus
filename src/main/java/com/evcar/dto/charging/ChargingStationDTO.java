package com.evcar.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargingStationDTO {

    private Long stationId;
    private String stationName;
    private double latitude;
    private double longitude;
    private String address;
}
