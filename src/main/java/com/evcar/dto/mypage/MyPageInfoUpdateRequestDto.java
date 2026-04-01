package com.evcar.dto.mypage;


import java.time.LocalDate;

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
	private LocalDate birthDate;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String addressDetail;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;
    private String hasVehicle;
    private String vehicleModel;
    private String vehicleYear;
    private Integer drivingDistance;

    public boolean hasPasswordChangeRequest() {
        return hasText(currentPassword) || hasText(newPassword) || hasText(newPasswordConfirm);
    }

    public boolean hasInvalidPasswordChangeInput() {
        return hasPasswordChangeRequest()
                && (!hasText(currentPassword) || !hasText(newPassword) || !hasText(newPasswordConfirm));
    }

    public boolean isNewPasswordMismatch() {
        return hasText(newPassword) && hasText(newPasswordConfirm) && !newPassword.equals(newPasswordConfirm);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}