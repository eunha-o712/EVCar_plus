<<<<<<< HEAD
package com.evcar.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupDto {

    private String loginId;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthDate;
    private String gender;
    private String phone;
    private String address;
    private String addressDetail;
    private String email;

    private String vehicleModel;
    private String vehicleYear;
    private Integer drivingDistance;

    @Builder.Default
    private boolean agreeTerms = false;

    @Builder.Default
    private boolean agreePrivacy = false;
=======
package com.evcar.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Setter
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}