package com.evcar.dto.mypage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfoUpdateRequestDto {

	private static final int MIN_PHONE_DIGITS = 10;
	private static final int MAX_PHONE_DIGITS = 15;
	private static final int MAX_PHONE_LENGTH = 16;
	private static final DateTimeFormatter VEHICLE_YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

	// 비밀번호: 영문/숫자만, 8~20자
	private static final String PASSWORD_REGEX = "^[A-Za-z0-9]{8,20}$";

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

	private String currentPassword;
	private String newPassword;
	private String newPasswordConfirm;

	public boolean isNovehicle() {
		return hasVehicle == null || "no".equalsIgnoreCase(hasVehicle) || "n".equalsIgnoreCase(hasVehicle);
	}

	public boolean hasPasswordChangeRequest() {
		return hasText(currentPassword) || hasText(newPassword) || hasText(newPasswordConfirm);
	}

	public boolean hasInvalidPasswordChangeInput() {
		return hasPasswordChangeRequest()
				&& (!hasText(currentPassword) || !hasText(newPassword) || !hasText(newPasswordConfirm));
	}

	public boolean isNewPasswordMismatch() {
		if (!hasText(newPassword) && !hasText(newPasswordConfirm)) {
			return false;
		}

		if (newPassword == null) {
			return newPasswordConfirm != null;
		}

		return !newPassword.equals(newPasswordConfirm);
	}

	// 추가: 새 비밀번호 형식 검사
	public boolean isInvalidNewPasswordFormat() {
		if (!hasText(newPassword)) {
			return false;
		}

		return !newPassword.trim().matches(PASSWORD_REGEX);
	}

	public boolean isInvalidPhone() {
		if (!hasText(phone)) {
			return false;
		}

		String normalizedPhone = normalizePhone(phone);
		String phoneDigits = extractPhoneDigits(normalizedPhone);

		if (!normalizedPhone.matches("^\\+?\\d+$")) {
			return true;
		}

		if (phoneDigits.length() < MIN_PHONE_DIGITS || phoneDigits.length() > MAX_PHONE_DIGITS) {
			return true;
		}

		return normalizedPhone.length() > MAX_PHONE_LENGTH;
	}

	public boolean isInvalidVehicleYear() {
		if (!hasText(vehicleYear)) {
			return false;
		}

		if (!vehicleYear.matches("^\\d{4}-(0[1-9]|1[0-2])$")) {
			return true;
		}

		try {
			YearMonth parsed = YearMonth.parse(vehicleYear, VEHICLE_YEAR_MONTH_FORMATTER);
			return parsed.getYear() < 1900;
		} catch (DateTimeParseException e) {
			return true;
		}
	}

	public String getNormalizedPhone() {
		return normalizePhone(phone);
	}

	private String normalizePhone(String value) {
		if (!hasText(value)) {
			return "";
		}

		String trimmed = value.trim();
		boolean hasPlusPrefix = trimmed.startsWith("+");
		String digitsOnly = trimmed.replaceAll("\\D", "");

		return hasPlusPrefix ? "+" + digitsOnly : digitsOnly;
	}

	private String extractPhoneDigits(String value) {
		if (value == null) {
			return "";
		}

		return value.replaceAll("\\D", "");
	}

	private boolean hasText(String value) {
		return value != null && !value.trim().isEmpty();
	}
}