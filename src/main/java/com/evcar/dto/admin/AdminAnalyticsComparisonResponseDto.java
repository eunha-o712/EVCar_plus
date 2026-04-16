package com.evcar.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAnalyticsComparisonResponseDto {

    private long currentConsultationCount;
    private long previousConsultationCount;
    private int consultationChangeRate;

    private int currentConversionRate;
    private int previousConversionRate;
    private int conversionRateGap;

    private String topModelName;
    private String topRegionName;
}