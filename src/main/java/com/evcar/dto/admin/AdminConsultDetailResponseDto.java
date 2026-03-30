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
public class AdminConsultDetailResponseDto {

    private String consultId;
    private String userId;
    private String userName;
    private String vehicleId;
    private String vehicleModelName;
    private String preferredDatetime;
    private Integer budget;
    private String purchasePlan;
    private String consultationContent;
    private String consultStatus;
    private String consultResult;
    private String adminReply;
    private String createdAt;
    private String updatedAt;
}