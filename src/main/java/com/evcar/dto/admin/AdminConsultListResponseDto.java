package com.evcar.dto.admin;

import com.evcar.common.enums.VehicleModelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdminConsultListResponseDto {

    private String consultId;
    private String userName;
    private String vehicleModelName;
    private String consultationDateTime;
    private String consultStatus;

    public String getVehicleModelNameLabel() {
        return VehicleModelType.toLabel(vehicleModelName);
    }
}