package com.evcar.dto.mypage;

import java.time.LocalDate;

import com.evcar.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfoResponseDto {
	
	private Integer userId;
	private String loginId;
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
	
	public static MyPageInfoResponseDto from(User user) {
		return MyPageInfoResponseDto.builder()
				.userId(user.getUserId())
				.loginId(user.getLoginId())
				.name(user.getName())
				.birthDate(user.getBirthDate())
				.gender(user.getGender())
				.phone(user.getPhone())
				.address(user.getAddress())
				.addressDetail(user.getAddressDetail())
				.email(user.getEmail())
				.hasVehicle(user.getHasVehicle())
				.vehicleModel(user.getVehicleModel())
				.vehicleYear(user.getVehicleYear())
				.drivingDistance(user.getDrivingDistance())
				.build();
	}
}
