package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserListResponseDto {

	private String userId;
    private String loginId;
    private String name;
    private String phone;
    private String email;
    private String userStatus;
    private String role;
    private Boolean hasVehicle;
    private Integer vehicleYear;
    private Integer drivingDistance;
    private LocalDateTime createdAt;
    private LocalDateTime withdrawnAt;
}