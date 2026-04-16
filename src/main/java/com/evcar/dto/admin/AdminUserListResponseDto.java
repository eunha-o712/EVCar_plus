package com.evcar.dto.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    public String getFormattedPhone() {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.startsWith("82")) {
            digits = "0" + digits.substring(2);
        }

        if (digits.length() == 11) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
        }

        if (digits.length() == 10) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 6) + "-" + digits.substring(6);
        }

        return phone;
    }

    public String getFormattedPhoneWithCountry() {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.startsWith("82")) {
            digits = digits.substring(2);
        }

        if (digits.startsWith("0")) {
            digits = digits.substring(1);
        }

        if (digits.length() == 10) {
            return "+82 " + digits.substring(0, 2) + "-" + digits.substring(2, 6) + "-" + digits.substring(6);
        }

        if (digits.length() == 9) {
            return "+82 " + digits.substring(0, 2) + "-" + digits.substring(2, 5) + "-" + digits.substring(5);
        }

        return "+82 " + phone;
    }

}