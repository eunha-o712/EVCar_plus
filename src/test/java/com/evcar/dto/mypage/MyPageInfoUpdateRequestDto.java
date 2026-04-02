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

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public boolean isNovehicle() {
        return hasVehicle == null
                || "no".equalsIgnoreCase(hasVehicle)
                || "N".equalsIgnoreCase(hasVehicle);
    }

    public boolean hasPasswordChangeRequest() {
        return hasText(currentPassword)
                || hasText(newPassword)
                || hasText(newPasswordConfirm);
    }

    public boolean hasInvalidPasswordChangeInput() {
        return hasPasswordChangeRequest()
                && (!hasText(currentPassword)
                || !hasText(newPassword)
                || !hasText(newPasswordConfirm));
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

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}