package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDetailResponseDto {

	private String userId;
    private String loginId;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private String userStatus;
    private String role;
    private Boolean hasVehicle;
    private Integer vehicleYear;
    private Integer drivingDistance;
    private LocalDateTime createdAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime updatedAt;
}