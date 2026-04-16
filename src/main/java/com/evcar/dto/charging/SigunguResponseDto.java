package com.evcar.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SigunguResponseDto {

    private String zscode;
    private String sigungu;
}