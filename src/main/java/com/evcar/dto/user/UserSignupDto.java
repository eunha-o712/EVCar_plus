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

}