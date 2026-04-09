package com.evcar.dto.mypage;

import com.evcar.domain.consultation.Consultation;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyConsultationResponseDto {

    private String consultId;
    private String preferredDatetime;
    private Integer budget;
    private String purchasePlan;
    private String consultContent;
    private String consultStatus;
    private String consultResult;
    private String adminReply;
    private LocalDate createdAt;
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
                .createdAt(consultation.getCreatedAt() == null ? null : consultation.getCreatedAt().toLocalDate())
                .cancelable(consultation.canBeCanceled())
                .build();
    }

    public String getConsultStatusLabel() {
        return switch (consultStatus) {
            case "PENDING" -> "대기";
            case "IN_PROGRESS" -> "진행중";
            case "COMPLETED" -> "완료";
            case "CANCELED" -> "취소";
            default -> consultStatus;
        };
    }

}