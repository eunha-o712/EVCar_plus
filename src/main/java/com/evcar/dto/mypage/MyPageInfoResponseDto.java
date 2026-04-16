package com.evcar.dto.mypage;

import com.evcar.domain.user.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfoResponseDto {

	private String userId;
	private String loginId;
	private String userStatus;
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
				.userStatus(user.getUserStatus().name())   // ← enum → String 변환
				.name(user.getName())
				.birthDate(user.getBirthDate())
				.gender(user.getGender())
				.phone(user.getPhone())
				.address(user.getAddress())
				.addressDetail(user.getAddressDetail())
				.email(user.getEmail())
				.hasVehicle(toHasVehicle(user))
				.vehicleModel(user.getVehicleModel())
				.vehicleYear(user.getVehicleYear())
				.drivingDistance(user.getDrivingDistance())
				.build();
	}

	private static String toHasVehicle(User user) {
		boolean hasVehicleInfo = (user.getVehicleModel() != null && !user.getVehicleModel().isBlank())
				|| (user.getVehicleYear() != null && !user.getVehicleYear().isBlank())
				|| (user.getDrivingDistance() != null && user.getDrivingDistance() > 0);

		return hasVehicleInfo ? "yes" : "no";
	}

	public String getFormattedPhone() {
	    if (phone == null || phone.isBlank()) {
	        return "";
	    }

	    String trimmed = phone.trim();

	    if (trimmed.startsWith("+82")) {
	        String digits = trimmed.substring(3).replaceAll("\\D", "");
	        if (digits.startsWith("0")) {
	            digits = digits.substring(1);
	        }
	        if (digits.length() == 10 && digits.startsWith("10")) {
	            return "+82 " + digits.substring(0, 2) + "-" + digits.substring(2, 6) + "-" + digits.substring(6);
	        }
	        if (digits.length() == 9 && digits.startsWith("2")) {
	            return "+82 " + digits.substring(0, 1) + "-" + digits.substring(1, 5) + "-" + digits.substring(5);
	        }
	        if (digits.length() == 9) {
	            return "+82 " + digits.substring(0, 2) + "-" + digits.substring(2, 5) + "-" + digits.substring(5);
	        }
	        if (digits.length() == 10) {
	            return "+82 " + digits.substring(0, 2) + "-" + digits.substring(2, 6) + "-" + digits.substring(6);
	        }
	        return trimmed;
	    }

	    String digits = trimmed.replaceAll("\\D", "");
	    if (digits.length() == 11 && digits.startsWith("010")) {
	        return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
	    }
	    if (digits.length() == 10 && digits.startsWith("02")) {
	        return digits.substring(0, 2) + "-" + digits.substring(2, 6) + "-" + digits.substring(6);
	    }
	    if (digits.length() == 9 && digits.startsWith("02")) {
	        return digits.substring(0, 2) + "-" + digits.substring(2, 5) + "-" + digits.substring(5);
	    }
	    if (digits.length() == 10) {
	        return digits.substring(0, 3) + "-" + digits.substring(3, 6) + "-" + digits.substring(6);
	    }
	    if (digits.length() == 11) {
	        return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
	    }
	    return trimmed;
	}
}