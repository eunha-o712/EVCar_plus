package com.evcar.dto.chatbot;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdminDashboardAiRequestDto {

    private long todayReservationCount;
    private long inProgressCount;
    private long completedCount;
    private List<MonthlyStatDto> monthlyStats;
    private List<TopVehicleStatDto> topVehicles;
    private List<RegionStatDto> regionStats;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MonthlyStatDto {
        private String monthLabel;
        private long consultationCount;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class TopVehicleStatDto {
        private String modelName;
        private long consultationCount;
        private double percentage;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class RegionStatDto {
        private String regionName;
        private long consultationCount;
        private double percentage;
    }
}