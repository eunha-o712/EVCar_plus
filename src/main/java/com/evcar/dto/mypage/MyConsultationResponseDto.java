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

    private String consultId;
    private String preferredDatetime;
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

    public String getConsultStatusLabel() {
        return switch (consultStatus) {
            case "PENDING" -> "대기";
            case "IN_PROGRESS" -> "진행중";
            case "COMPLETED" -> "완료";
            case "CANCELED" -> "취소";
            default -> consultStatus;
        };
    }

    public String getPurchasePlanLabel() {
        return switch (purchasePlan) {
            case "IMMEDIATE" -> "즉시";
            case "1_MONTH" -> "1개월 이내";
            case "3_MONTH" -> "3개월 이내";
            case "6_MONTH" -> "6개월 이내";
            case "UNDECIDED" -> "미정";
            default -> purchasePlan;
        };
    }

    public String getConsultResultLabel() {
        if (consultResult == null || consultResult.isBlank()) {
            return "-";
        }

        return switch (consultResult) {
            case "PURCHASE" -> "구매";
            case "NOT_PURCHASE" -> "미구매";
            case "UNDECIDED" -> "미정";
            default -> consultResult;
        };
    }
}