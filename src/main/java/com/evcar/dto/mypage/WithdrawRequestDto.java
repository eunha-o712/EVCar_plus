package com.evcar.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawRequestDto {

    private String password;
    private String withdrawReason;
    private String withdrawReasonDetail;
    private Boolean agreeWithdraw;

    public boolean isInvalid() {
        if (!hasText(password)) {
            return true;
        }

        if (!hasText(withdrawReason)) {
            return true;
        }

        if (Boolean.TRUE.equals(isOtherReason()) && !hasText(withdrawReasonDetail)) {
            return true;
        }

        return !Boolean.TRUE.equals(agreeWithdraw);
    }

    public String getFinalWithdrawReason() {
        if (Boolean.TRUE.equals(isOtherReason())) {
            return withdrawReasonDetail.trim();
        }
        return withdrawReason.trim();
    }

    public boolean isOtherReason() {
        return "기타".equals(withdrawReason);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}