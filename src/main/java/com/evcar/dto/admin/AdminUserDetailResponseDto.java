package com.evcar.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String vehicleModel;
    private String vehicleYear;
    private Integer drivingDistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getHasVehicle() {
        boolean hasModel = vehicleModel != null && !vehicleModel.isBlank();
        boolean hasYear = vehicleYear != null && !vehicleYear.isBlank();
        boolean hasDistance = drivingDistance != null && drivingDistance > 0;
        return (hasModel || hasYear || hasDistance) ? "yes" : "no";
    }

    public String getFormattedPhoneWithCountry() {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.startsWith("82")) {
            digits = digits.substring(2);
        }

        if (!digits.startsWith("0")) {
            digits = "0" + digits;
        }

        if (digits.length() == 11) {
            return "+82 "
                    + digits.substring(1, 3) + "-"
                    + digits.substring(3, 7) + "-"
                    + digits.substring(7);
        }

        if (digits.length() == 10) {
            return "+82 "
                    + digits.substring(1, 3) + "-"
                    + digits.substring(3, 6) + "-"
                    + digits.substring(6);
        }

        return "+82 " + phone;
    }
}