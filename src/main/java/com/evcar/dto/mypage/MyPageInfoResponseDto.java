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
    private String vehicleModel;
    private String vehicleYear;
    private Integer drivingDistance;

    public static MyPageInfoResponseDto from(User user) {
        return MyPageInfoResponseDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .userStatus(user.getUserStatus().name())
                .name(user.getName())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .email(user.getEmail())
                .vehicleModel(user.getVehicleModel())
                .vehicleYear(user.getVehicleYear())
                .drivingDistance(user.getDrivingDistance())
                .build();
    }

    public String getHasVehicle() {
        boolean hasModel = vehicleModel != null && !vehicleModel.isBlank();
        boolean hasYear = vehicleYear != null && !vehicleYear.isBlank();
        boolean hasDistance = drivingDistance != null && drivingDistance > 0;
        return (hasModel || hasYear || hasDistance) ? "yes" : "no";
    }
}

