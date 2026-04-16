package com.evcar.dto.admin;

import java.util.List;

import com.evcar.common.enums.VehicleModelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponseDto {

    private long todayReservationCount;
    private long inProgressCount;
    private long completedCount;
    private List<MonthlyConsultationStatDto> monthlyConsultationStats;
    private List<TopVehicleStatDto> topVehicleStats;
    private List<RegionConsultationStatDto> regionConsultationStats;

    private String aiSummary;
    private String aiTrend;
    private String aiVehicle;
    private String aiRegion;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyConsultationStatDto {
        private String monthLabel;
        private long consultationCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopVehicleStatDto {
        private String modelName;
        private long consultationCount;
        private double percentage;

        public String getModelNameLabel() {
            return VehicleModelType.toLabel(modelName);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegionConsultationStatDto {
        private String regionName;
        private long consultationCount;
        private double percentage;
    }
}