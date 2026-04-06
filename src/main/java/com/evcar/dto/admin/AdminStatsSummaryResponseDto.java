package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsSummaryResponseDto {

    private long totalConsultationCount;
    private long monthlyConsultationCount;
    private int contractConversionRate;
}