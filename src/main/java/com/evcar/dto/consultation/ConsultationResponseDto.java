package com.evcar.dto.consultation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ConsultationResponseDto {

    private String consultId;
    private String userId;
    private String userName;
    private String vehicleId;
    private String vehicleModelName;
    private String preferredDatetime;
    private Integer budget;
    private String purchasePlan;
    private String consultContent;
    private String consultStatus;
    private String consultResult;
    private String adminReply;
    private LocalDateTime createdAt;
}