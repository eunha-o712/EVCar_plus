package com.evcar.dto.mypage;

import com.evcar.domain.consultation.Consultation;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyConsultationResponseDto {

    private Integer consultId;
    private LocalDateTime preferredDatetime;
    private Integer budget;
    private String purchasePlan;
    private String consultContent;
    private String consultStatus;
    private String consultResult;
    private String adminReply;
    private LocalDateTime createdAt;
    private boolean cancelable;

    public static MyConsultationResponseDto from(Consultation consultation) {
        return MyConsultationResponseDto.builder()
                .consultId(consultation.getConsultId())
                .preferredDatetime(consultation.getPreferredDatetime())
                .budget(consultation.getBudget())
                .purchasePlan(consultation.getPurchasePlan())
                .consultContent(consultation.getConsultContent())
                .consultStatus(consultation.getConsultStatus())
                .consultResult(consultation.getConsultResult())
                .adminReply(consultation.getAdminReply())
                .createdAt(consultation.getCreatedAt())
                .cancelable(consultation.canBeCanceled())
                .build();
    }
}