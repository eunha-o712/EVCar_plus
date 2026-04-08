package com.evcar.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargerResponseDto {

    private String chargerType; // 충전기 타입
    private String status;      // 상태
}