package com.evcar.dto.vehicle;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAiRecommendRequestDto {

    private String userMessage;
    private List<VehicleAiVehicleDto> vehicles;
}