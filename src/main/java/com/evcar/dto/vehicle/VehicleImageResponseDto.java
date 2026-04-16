package com.evcar.dto.vehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VehicleImageResponseDto {

    private String imageId;
    private String vehicleId;
    private String imageUrl;
    private Integer imageOrder;
}