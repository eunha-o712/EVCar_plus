<<<<<<< HEAD
package com.evcar.dto.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdminUserListResponseDto {

    private String userId;
    private String loginId;
    private String name;
    private String phone;
    private String email;
    private String userStatus;
    private String createdAt;

    public String getFormattedPhone() {
        if (phone == null) {
            return "";
        }

        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.length() == 11) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
        }

        if (digits.length() == 10) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 6) + "-" + digits.substring(6);
        }

        return phone;
    }
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}