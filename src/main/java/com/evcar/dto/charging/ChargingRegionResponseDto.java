package com.evcar.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ChargingRegionResponseDto {

    private String zcode;
    private String sido;
    private List<SigunguResponseDto> sigunguList;
}