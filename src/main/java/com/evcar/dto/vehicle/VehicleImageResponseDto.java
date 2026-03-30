package com.evcar.dto.vehicle;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleImageResponseDto {

    private String imageUrl;

    public static VehicleImageResponseDto from(String imageUrl) {
        return VehicleImageResponseDto.builder()
                .imageUrl(imageUrl)
                .build();
    }
}