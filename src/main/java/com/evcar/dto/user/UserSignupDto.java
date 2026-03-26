package com.evcar.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupDto {

    private String loginId;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthDate; // YYYY-MM-DD
    private String gender;
    private String phone;
    private String address;
    private String addressDetail;
    private String email;
    
    // 차량 정보 (선택)
    private String vehicleModel;
    private String vehicleYear;
    private Integer drivingDistance;

    // 약관 동의
    private boolean agreeTerms;
    private boolean agreePrivacy;
}