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
		return MyPageInfoResponseDto.builder().userId(user.getUserId()).loginId(user.getLoginId())
				.userStatus(user.getUserStatus()).name(user.getName()).birthDate(user.getBirthDate())
				.gender(user.getGender()).phone(user.getPhone()).address(user.getAddress())
				.addressDetail(user.getAddressDetail()).email(user.getEmail()).hasVehicle(toHasVehicle(user))
				.vehicleModel(user.getVehicleModel()).vehicleYear(user.getVehicleYear())
				.drivingDistance(user.getDrivingDistance()).build();
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

	    // 1) 한국 국제번호(+82) 처리
	    if (trimmed.startsWith("+82")) {
	        String digits = trimmed.substring(3).replaceAll("\\D", "");

	        // +82 뒤에 0이 남아 있으면 제거
	        if (digits.startsWith("0")) {
	            digits = digits.substring(1);
	        }

	        // 한국 휴대폰 번호: 10xxxxxxxx
	        if (digits.length() == 10 && digits.startsWith("10")) {
	            return "+82 " + digits.substring(0, 2) + "-"
	                    + digits.substring(2, 6) + "-"
	                    + digits.substring(6);
	        }

	        // 서울 지역번호: 2xxxxxxxx
	        if (digits.length() == 9 && digits.startsWith("2")) {
	            return "+82 " + digits.substring(0, 1) + "-"
	                    + digits.substring(1, 5) + "-"
	                    + digits.substring(5);
	        }

	        // 그 외 한국 번호(예: 31, 32 등 지역번호)
	        if (digits.length() == 9) {
	            return "+82 " + digits.substring(0, 2) + "-"
	                    + digits.substring(2, 5) + "-"
	                    + digits.substring(5);
	        }

	        if (digits.length() == 10) {
	            return "+82 " + digits.substring(0, 2) + "-"
	                    + digits.substring(2, 6) + "-"
	                    + digits.substring(6);
	        }

	        // 한국 번호로 보기 애매하면 원본 유지
	        return trimmed;
	    }

	    // 2) 국내 한국 번호 처리
	    String digits = trimmed.replaceAll("\\D", "");

	    // 010 휴대폰 번호
	    if (digits.length() == 11 && digits.startsWith("010")) {
	        return digits.substring(0, 3) + "-"
	                + digits.substring(3, 7) + "-"
	                + digits.substring(7);
	    }

	    // 서울번호 02
	    if (digits.length() == 10 && digits.startsWith("02")) {
	        return digits.substring(0, 2) + "-"
	                + digits.substring(2, 6) + "-"
	                + digits.substring(6);
	    }

	    if (digits.length() == 9 && digits.startsWith("02")) {
	        return digits.substring(0, 2) + "-"
	                + digits.substring(2, 5) + "-"
	                + digits.substring(5);
	    }

	    // 그 외 국내 지역번호
	    if (digits.length() == 10) {
	        return digits.substring(0, 3) + "-"
	                + digits.substring(3, 6) + "-"
	                + digits.substring(6);
	    }

	    if (digits.length() == 11) {
	        return digits.substring(0, 3) + "-"
	                + digits.substring(3, 7) + "-"
	                + digits.substring(7);
	    }

	    // 3) 외국 번호나 알 수 없는 형식은 원본 그대로
	    return trimmed;
	}
}