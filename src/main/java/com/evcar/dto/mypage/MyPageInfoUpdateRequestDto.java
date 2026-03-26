package com.evcar.dto.mypage;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfoUpdateRequestDto {
	
	private String name;
	private LocalDate birthDate;
	private String gender;
	private String phone;
	private String address;
	private String addressDetail;
	private String email;
	private String hasVehicle;
	private String vehicleModel;
	private String vehicleYear;
	private Integer drivingDistance;
	
	public boolean isNovehicle() {
		return hasVehicle==null|| "no".equalsIgnoreCase(hasVehicle)||"N".equalsIgnoreCase(hasVehicle);
	}
}
